package space.maxus.dnevnik.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Type;
import space.maxus.dnevnik.controllers.response.ResponseSubject;
import space.maxus.dnevnik.data.fetch.AggregatorService;
import space.maxus.dnevnik.data.hibernate.UUIDArrayType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subjects")
@Data
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "subjects_id_seq")
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "teacher_ids", nullable = false)
    @Type(UUIDArrayType.class)
    private UUID[] teacherIds;

    public Subject(String name, List<UUID> teacherIds) {
        this.name = name;
        this.teacherIds = teacherIds.toArray(new UUID[0]);
    }

    public Subject() {

    }

    public List<Teacher> getTeachers() {
        var asList = Arrays.asList(teacherIds);
        return AggregatorService.INSTANCE.getTeacherService().findAll().stream().filter(t -> asList.contains(t.getId())).toList();
    }

    public ResponseSubject response() {
        return new ResponseSubject(id, name, Arrays.asList(teacherIds));
    }
}
