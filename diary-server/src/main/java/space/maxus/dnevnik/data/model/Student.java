package space.maxus.dnevnik.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "students")
@Data @NoArgsConstructor(force = true) @AllArgsConstructor @RequiredArgsConstructor
public class Student {
    @Id
    private UUID id = UUID.randomUUID();

    private final String name;
    private final String surname;
    private final String email;
    private final String passHash;
}
