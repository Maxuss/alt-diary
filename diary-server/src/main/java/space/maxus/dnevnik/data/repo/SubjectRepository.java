package space.maxus.dnevnik.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import space.maxus.dnevnik.data.model.Subject;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {

}