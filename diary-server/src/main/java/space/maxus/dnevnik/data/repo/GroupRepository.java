package space.maxus.dnevnik.data.repo;

import org.springframework.data.repository.CrudRepository;
import space.maxus.dnevnik.data.model.Group;

public interface GroupRepository extends CrudRepository<Group, Long> {
}
