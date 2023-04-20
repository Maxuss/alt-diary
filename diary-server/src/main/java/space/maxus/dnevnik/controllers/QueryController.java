package space.maxus.dnevnik.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.maxus.dnevnik.controllers.response.BiQueryResponse;
import space.maxus.dnevnik.controllers.response.ResponseLesson;
import space.maxus.dnevnik.data.model.Lesson;
import space.maxus.dnevnik.data.service.LessonService;

import java.util.List;

@RestController
public class QueryController {
    private final LessonService lessons;

    public QueryController(LessonService lessons) {
        this.lessons = lessons;
    }

    @GetMapping("/lessons")
    public BiQueryResponse<List<ResponseLesson>, ResponseLesson> getLessons() {
        return BiQueryResponse.left(lessons.findAll().stream().map(Lesson::response).toList());
    }

    @GetMapping("/lessons/{id}")
    public BiQueryResponse<ResponseLesson, Void> getLesson(@PathVariable("id") Long id) {
        return lessons.findById(id).map(found -> BiQueryResponse.<ResponseLesson, Void>left(found.response())).orElse(BiQueryResponse.neither("Could not find lesson with id " + id));
    }
}
