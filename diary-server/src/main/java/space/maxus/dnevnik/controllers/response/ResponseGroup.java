package space.maxus.dnevnik.controllers.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ResponseGroup {
    private final long id;
    private final String name;
    @JsonManagedReference
    private final List<ResponseStudent> students;
    @JsonBackReference
    private final ResponseTeacher leaderTeacher;
    private UUID leaderTeacherId;
}
