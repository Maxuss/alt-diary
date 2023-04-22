package space.maxus.dnevnik.data.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.model.Schedule;
import space.maxus.dnevnik.data.repo.ScheduleRepository;

@Service
public class ScheduleService extends AbstractCrudService<Schedule, Long, ScheduleRepository> {
    @Getter
    private final ScheduleRepository repository;

    public ScheduleService(ScheduleRepository repository) {
        this.repository = repository;
    }
}
