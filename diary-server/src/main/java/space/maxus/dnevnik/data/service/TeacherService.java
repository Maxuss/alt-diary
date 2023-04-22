package space.maxus.dnevnik.data.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.model.Teacher;
import space.maxus.dnevnik.data.repo.TeacherRepository;

import java.util.UUID;

@Service
public class TeacherService extends AbstractCrudService<Teacher, UUID, TeacherRepository> {
    @Getter
    private final TeacherRepository repository;

    public TeacherService(TeacherRepository repository) {
        this.repository = repository;
    }
}
