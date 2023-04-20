package space.maxus.dnevnik.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "teachers")
@Data @NoArgsConstructor(force = true) @AllArgsConstructor @RequiredArgsConstructor
public class Teacher {

    @Id
    private UUID id = UUID.randomUUID();

    private final String name;
    private final String surname;
    private final String patronymic;
    private final String email;
    private final String passHash;

    @OneToMany(mappedBy = "leaderTeacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Group> groups;
}
