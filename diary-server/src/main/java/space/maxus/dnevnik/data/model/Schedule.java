package space.maxus.dnevnik.data.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "schedules_id_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
}
