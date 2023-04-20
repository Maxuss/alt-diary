package space.maxus.dnevnik.controllers.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ResponseTeacher {
    private final UUID id;
    private final String name;
    private final String patronymic;
    private final String surname;
    private final String email;
    @JsonManagedReference
    private final List<ResponseGroup> managedGroups;
}
