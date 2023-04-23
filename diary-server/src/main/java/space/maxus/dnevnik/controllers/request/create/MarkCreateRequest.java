package space.maxus.dnevnik.controllers.request.create;

import lombok.Data;
import space.maxus.dnevnik.data.model.Mark;

import java.util.UUID;

@Data
public class MarkCreateRequest {
    private String message;
    private Mark.Kind kind = Mark.Kind.GENERIC;
    private byte value;
    private byte index = 1;
    private long lessonId;
    private UUID studentId;
}
