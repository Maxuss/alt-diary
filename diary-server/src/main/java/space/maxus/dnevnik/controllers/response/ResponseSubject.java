package space.maxus.dnevnik.controllers.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ResponseSubject {
    private final long id;
    private final String name;
    private final List<UUID> teachers;
}
