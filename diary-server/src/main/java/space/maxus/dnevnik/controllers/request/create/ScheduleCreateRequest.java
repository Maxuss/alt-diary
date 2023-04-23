package space.maxus.dnevnik.controllers.request.create;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleCreateRequest {
    private long groupId;
    private byte dayOfWeek;
    private List<Long> subjects;
    private List<Long> breakDurations;
}
