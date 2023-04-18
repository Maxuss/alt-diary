package space.maxus.dnevnik.data.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "attachments")
@Data @NoArgsConstructor(force = true) @RequiredArgsConstructor @AllArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final String fileName;
}
