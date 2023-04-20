package space.maxus.dnevnik.data.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.model.Lesson;
import space.maxus.dnevnik.data.repo.LessonRepository;

@Service
public class LessonService extends AbstractCrudService<Lesson, Long, LessonRepository> {
    @Getter
    private final LessonRepository repository;

    public LessonService(LessonRepository repository) {
        this.repository = repository;
    }
}
