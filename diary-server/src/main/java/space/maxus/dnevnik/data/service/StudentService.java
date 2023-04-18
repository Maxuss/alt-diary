package space.maxus.dnevnik.data.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.model.Student;
import space.maxus.dnevnik.data.repo.StudentRepository;

import java.util.UUID;

@Service
public class StudentService extends AbstractCrudService<Student, UUID, StudentRepository> {
    @Getter
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }
}
