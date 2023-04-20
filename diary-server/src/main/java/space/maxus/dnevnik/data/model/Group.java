package space.maxus.dnevnik.data.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import space.maxus.dnevnik.data.fetch.AggregatorService;
import space.maxus.dnevnik.data.hibernate.UUIDArrayType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Data @NoArgsConstructor(force = true) @RequiredArgsConstructor @AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "$id")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "groups_id_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private final String name;

    @ManyToOne
    @JoinColumn(columnDefinition = "leader_teacher_id")
    private final Teacher leaderTeacher;

    @Type(UUIDArrayType.class)
    private final UUID[] studentsIds;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules;

    public List<Student> getStudents() {
        var asList = Arrays.asList(studentsIds);
        return AggregatorService.INSTANCE.getStudentService().findAll().stream().filter(each -> asList.contains(each.getId())).toList();
    }
}
