package space.maxus.dnevnik.controllers.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class LessonsRequest {
    private @DateTimeFormat(pattern = "yyyy-MM-dd") Date date;
    private long groupId;
}
