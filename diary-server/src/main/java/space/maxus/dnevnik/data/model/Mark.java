package space.maxus.dnevnik.data.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import space.maxus.dnevnik.controllers.response.ResponseMark;

import java.util.UUID;

@Entity
@Table(name = "marks")
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "$id")
public class Mark {
    private final byte value;
    @Column(name = "idx")
    private final byte index;
    @Enumerated(EnumType.STRING)
    private final Kind kind;
    private final String message;
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private final Lesson lesson;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private final Student student;
    private final UUID teacherId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "marks_id_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    public ResponseMark response() {
        return new ResponseMark(id, value, index, message, kind, teacherId, student == null ? null : student.getId());
    }

    public enum Kind {
        GENERIC,
        EXAM,
        POINT
    }
}
