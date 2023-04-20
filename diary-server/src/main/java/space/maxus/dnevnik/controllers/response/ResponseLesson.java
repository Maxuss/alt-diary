package space.maxus.dnevnik.controllers.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ResponseLesson {
    private final long id;
    private final ResponseSubject subject;
    @JsonManagedReference
    private final List<ResponseMark> marks;
    private final ResponseHomework homework;
    private final Date date;
}
