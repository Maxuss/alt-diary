package space.maxus.dnevnik.controllers.response;

import lombok.Data;
import space.maxus.dnevnik.data.model.Attachment;

import java.util.List;
import java.util.UUID;

@Data
public class ResponseHomework {
    private final long id;
    private final boolean isNone;
    private final String summary;
    private final UUID teacherId;
    private final List<Attachment> attachments;
}
