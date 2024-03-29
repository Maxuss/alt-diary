package space.maxus.dnevnik.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import space.maxus.dnevnik.data.model.Student;

import java.util.UUID;

@Repository
public interface StudentRepository extends CrudRepository<Student, UUID> {

}