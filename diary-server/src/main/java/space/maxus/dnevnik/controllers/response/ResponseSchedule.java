package space.maxus.dnevnik.controllers.response;

import lombok.Data;

import java.util.List;

@Data
public class ResponseSchedule {
    private final long id;
    private final long groupId;
    private final byte dayOfWeek;
    private final List<ResponseSubject> subjects;
    private final List<Long> breakDurations;
}
