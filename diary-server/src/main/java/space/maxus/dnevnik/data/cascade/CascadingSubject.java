package space.maxus.dnevnik.data.cascade;

import space.maxus.dnevnik.data.model.Teacher;

import java.util.List;

public record CascadingSubject(Long id, String name, List<Teacher> teachers) { }