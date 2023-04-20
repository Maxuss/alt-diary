package space.maxus.dnevnik.controllers.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import space.maxus.dnevnik.data.model.Mark;

import java.util.UUID;

@Data
public class ResponseMark {
    private final long id;
    private final byte value;
    private final byte index;
    private final Mark.Kind kind;
    private final UUID teacherId;
    @JsonBackReference
    private ResponseLesson lesson;
    @JsonBackReference
    private ResponseStudent student;
}
