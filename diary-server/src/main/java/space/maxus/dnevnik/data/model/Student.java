package space.maxus.dnevnik.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "students")
@Data @NoArgsConstructor(force = true) @RequiredArgsConstructor @AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private final String name;
    private final String surname;
    private final String email;
    private final String passHash;
}
