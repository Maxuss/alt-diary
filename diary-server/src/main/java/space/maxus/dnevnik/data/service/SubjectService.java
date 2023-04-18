package space.maxus.dnevnik.data.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.model.Subject;
import space.maxus.dnevnik.data.repo.SubjectRepository;

@Service
public class SubjectService extends AbstractCrudService<Subject, Long, SubjectRepository> {
    @Getter
    private final SubjectRepository repository;

    public SubjectService(SubjectRepository repository) {
        this.repository = repository;
    }
}
