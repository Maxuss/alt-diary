package space.maxus.dnevnik.data.cascade;

import space.maxus.dnevnik.data.model.Student;
import space.maxus.dnevnik.data.model.Teacher;

import java.util.List;

public record CascadingGroup(
        long id,
        String name,
        Teacher leadingTeacher,
        List<Student> students
) { }
