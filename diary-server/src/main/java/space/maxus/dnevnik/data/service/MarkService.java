package space.maxus.dnevnik.data.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.model.Mark;
import space.maxus.dnevnik.data.repo.MarkRepository;

@Service
public class MarkService extends AbstractCrudService<Mark, Long, MarkRepository> {
    @Getter
    private final MarkRepository repository;

    public MarkService(MarkRepository repository) {
        this.repository = repository;
    }
}
