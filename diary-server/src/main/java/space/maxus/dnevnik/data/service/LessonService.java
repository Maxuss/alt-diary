package space.maxus.dnevnik.data.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.fetch.AggregatorService;
import space.maxus.dnevnik.data.model.Lesson;
import space.maxus.dnevnik.data.model.Subject;
import space.maxus.dnevnik.data.repo.LessonRepository;

import java.time.LocalTime;

@Service
public class LessonService extends AbstractCrudService<Lesson, Long, LessonRepository> {
    @Getter
    private final LessonRepository repository;

    public LessonService(LessonRepository repository) {
        this.repository = repository;
    }

    public Lesson newEmpty(Subject base, LocalTime begin, LocalTime end) {
        Lesson empty = new Lesson(
                base.getId(),
                AggregatorService.INSTANCE.getHomeworkService().newEmpty(base.getTeacherIds()[0]).getId(),
                begin,
                end
        );
        repository.save(empty);
        return empty;
    }
}
