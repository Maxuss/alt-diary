package space.maxus.dnevnik.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import space.maxus.dnevnik.data.model.Homework;

@Repository
public interface HomeworkRepository extends CrudRepository<Homework, Long> {
}
