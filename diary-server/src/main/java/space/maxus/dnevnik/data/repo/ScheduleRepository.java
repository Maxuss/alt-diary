package space.maxus.dnevnik.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import space.maxus.dnevnik.data.model.Schedule;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
}
