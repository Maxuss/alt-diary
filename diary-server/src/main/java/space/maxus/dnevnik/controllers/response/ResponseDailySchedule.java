package space.maxus.dnevnik.controllers.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ResponseDailySchedule {
    private final Date date;
    private final long scheduleId;
    private final List<ResponseLesson> lessons;
}
