package space.maxus.dnevnik.controllers.request.create;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GroupCreateRequest {
    private String name;
    private List<UUID> students;
}
