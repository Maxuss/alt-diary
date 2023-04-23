package space.maxus.dnevnik.controllers.request.create;

import lombok.Data;

import java.util.List;

@Data
public class HomeworkCreateRequest {
    private long lessonId;
    private boolean isNone;
    private String summary;
    private List<Long> attachments;
}
