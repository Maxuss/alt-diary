package space.maxus.dnevnik.data.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "teachers")
@Data @NoArgsConstructor(force = true) @AllArgsConstructor @RequiredArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "$id")
public class Teacher {

    @Id
    private UUID id = UUID.randomUUID();

    private final String name;
    private final String surname;
    private final String patronymic;
    private final String email;
    private final String passHash;

    @OneToMany(mappedBy = "leaderTeacher")
    private List<Group> groups;
}
