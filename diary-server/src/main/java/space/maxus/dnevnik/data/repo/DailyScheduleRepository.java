package space.maxus.dnevnik.data.repo;

import org.springframework.data.repository.CrudRepository;
import space.maxus.dnevnik.data.model.DailySchedule;

import java.util.Date;

public interface DailyScheduleRepository extends CrudRepository<DailySchedule, Date> {
}
