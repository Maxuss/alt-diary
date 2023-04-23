package space.maxus.dnevnik.data.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import space.maxus.dnevnik.controllers.response.ResponseDailySchedule;
import space.maxus.dnevnik.controllers.response.ResponseLesson;
import space.maxus.dnevnik.data.fetch.AggregatorService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "daily_schedules")
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class DailySchedule {
    @Id
    private final Date date;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private final Schedule schedule;
    private final long[] lessonIds;

    public ResponseDailySchedule response() {
        AtomicInteger breakIdx = new AtomicInteger(0);
        if (schedule == null)
            return null;
        long[] breaks = schedule.getBreakDurations();
        List<ResponseLesson> lessons = Arrays.stream(ArrayUtils.toObject(lessonIds))
                .map(eachId -> {
                    try {
                        return AggregatorService.INSTANCE.getLessonService().findById(eachId).orElseThrow().response(breaks[breakIdx.getAndIncrement()]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return AggregatorService.INSTANCE.getLessonService().findById(eachId).orElseThrow().response(0);
                    }
                })
                .toList();
        return new ResponseDailySchedule(
                date,
                schedule.getId(),
                lessons
        );
    }
}
