package space.maxus.dnevnik.controllers.request.create;

import lombok.Data;
import space.maxus.dnevnik.data.model.Mark;

import java.util.UUID;

@Data
public class MarkCreateRequest {
    private String message;
    private Mark.Kind kind = Mark.Kind.GENERIC;
    private int value;
    private int index = 1;
    private long lessonId;
    private UUID studentId;
}
