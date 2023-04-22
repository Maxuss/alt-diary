package space.maxus.dnevnik.data.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.model.Group;
import space.maxus.dnevnik.data.repo.GroupRepository;

@Service
public class GroupService extends AbstractCrudService<Group, Long, GroupRepository> {
    @Getter
    private final GroupRepository repository;

    public GroupService(GroupRepository repository) {
        this.repository = repository;
    }
}
