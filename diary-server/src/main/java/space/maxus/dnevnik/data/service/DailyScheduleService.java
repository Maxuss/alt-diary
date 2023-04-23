package space.maxus.dnevnik.data.service;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.fetch.AggregatorService;
import space.maxus.dnevnik.data.model.DailySchedule;
import space.maxus.dnevnik.data.model.Schedule;
import space.maxus.dnevnik.data.repo.DailyScheduleRepository;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Log4j2
public class DailyScheduleService extends AbstractCrudService<DailySchedule, Date, DailyScheduleRepository> {
    public static final List<Pair<LocalTime, LocalTime>> DEFAULT_LESSON_TIMINGS = List.of(
            Pair.of(LocalTime.of(8, 30), LocalTime.of(9, 15)), // lesson 1
            Pair.of(LocalTime.of(9, 30), LocalTime.of(10, 10)), // lesson 2 + breakfast
            Pair.of(LocalTime.of(10, 30), LocalTime.of(11, 15)), // lesson 3
            Pair.of(LocalTime.of(11, 30), LocalTime.of(12, 10)), // lesson 4 + lunch
            Pair.of(LocalTime.of(12, 30), LocalTime.of(13, 15)), // lesson 5
            Pair.of(LocalTime.of(13, 30), LocalTime.of(14, 15)), // lesson 6
            Pair.of(LocalTime.of(14, 30), LocalTime.of(15, 15)), // lesson 7
            Pair.of(LocalTime.of(15, 30), LocalTime.of(16, 15)) // lesson 8
    );
    @Getter
    private final DailyScheduleRepository repository;

    public DailyScheduleService(DailyScheduleRepository repository) {
        this.repository = repository;
    }

    public DailySchedule scheduleForDate(Schedule base, Date date) {
        AtomicInteger timingIdx = new AtomicInteger(0);
        return repository.findById(date)
                .orElseGet(() -> {
                    DailySchedule schedule = new DailySchedule(
                            date,
                            base,
                            Arrays.stream(base.getSubjectIds()).map(id ->
                                    AggregatorService.INSTANCE.getLessonService().newEmpty(
                                            AggregatorService.INSTANCE.getSubjectService().findById(id).orElseThrow(),
                                            DEFAULT_LESSON_TIMINGS.get(timingIdx.get()).getLeft(),
                                            DEFAULT_LESSON_TIMINGS.get(timingIdx.getAndIncrement()).getRight()
                                    ).getId()
                            ).toArray()
                    );
                    repository.save(schedule);
                    return schedule;
                });
    }
}
