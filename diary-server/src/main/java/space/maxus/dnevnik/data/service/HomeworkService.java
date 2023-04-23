package space.maxus.dnevnik.data.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.model.Homework;
import space.maxus.dnevnik.data.repo.HomeworkRepository;

import java.util.UUID;

@Service
public class HomeworkService extends AbstractCrudService<Homework, Long, HomeworkRepository> {
    @Getter
    private final HomeworkRepository repository;

    public HomeworkService(HomeworkRepository repository) {
        this.repository = repository;
    }

    public Homework newEmpty(UUID teacher) {
        Homework empty = new Homework(true, "", teacher, new long[0]);
        repository.save(empty);
        return empty;
    }
}
