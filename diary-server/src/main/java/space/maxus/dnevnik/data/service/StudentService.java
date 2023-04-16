package space.maxus.dnevnik.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.model.Student;
import space.maxus.dnevnik.data.repo.StudentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {
    @Autowired
    private StudentRepository repository;

    public List<Student> findAll() {
        return (List<Student>) repository.findAll();
    }

    public void insertUpdate(Student student) {
        repository.save(student);
    }

    public void delete(Student student) {
        repository.delete(student);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
