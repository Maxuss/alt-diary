package space.maxus.dnevnik.data.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.model.Homework;
import space.maxus.dnevnik.data.repo.HomeworkRepository;

@Service
public class HomeworkService extends AbstractCrudService<Homework, Long, HomeworkRepository> {
    @Getter
    private final HomeworkRepository repository;

    public HomeworkService(HomeworkRepository repository) {
        this.repository = repository;
    }
}
