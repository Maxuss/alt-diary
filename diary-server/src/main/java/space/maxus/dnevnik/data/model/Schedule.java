package space.maxus.dnevnik.data.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import space.maxus.dnevnik.controllers.response.ResponseSchedule;
import space.maxus.dnevnik.data.fetch.AggregatorService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "$id")
public class Schedule {
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private final Group group;
    private final byte dayOfWeek;
    @Column(name = "subjects")
    private final long[] subjectIds;
    @Column(name = "breaks")
    private final long[] breakDurations;
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private final List<DailySchedule> dailySchedules;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "schedules_id_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    public ResponseSchedule response() {
        return new ResponseSchedule(id, Objects.requireNonNull(group).getId(), dayOfWeek,
                Arrays.stream(ArrayUtils.toObject(subjectIds))
                        .map(each -> AggregatorService.INSTANCE.getSubjectService().findById(each).orElse(null))
                        .filter(Objects::nonNull)
                        .map(Subject::response)
                        .toList(),
                Arrays.asList(ArrayUtils.toObject(breakDurations))
        );
    }
}
