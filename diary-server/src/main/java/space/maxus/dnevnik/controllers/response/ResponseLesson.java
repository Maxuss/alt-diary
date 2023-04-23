package space.maxus.dnevnik.controllers.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class ResponseLesson {
    private final long id;
    private final LocalTime beginTime;
    private final LocalTime endTime;
    private final long nextBreakDuration;
    private final ResponseSubject subject;
    @JsonManagedReference
    private final List<ResponseMark> marks;
    private final ResponseHomework homework;
}
