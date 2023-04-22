package space.maxus.dnevnik.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "attachments")
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class Attachment {
    private final String fileName;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "attachments_id_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
}
