package space.maxus.dnevnik.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.maxus.dnevnik.controllers.response.QueryResponse;
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
    public QueryResponse<List<ResponseLesson>, ResponseLesson> getLessons() {
        return QueryResponse.left(lessons.findAll().stream().map(Lesson::response).toList());
    }

    @GetMapping("/lessons/{id}")
    public QueryResponse<ResponseLesson, Void> getLesson(@PathVariable("id") Long id) {
        return lessons.findById(id).map(found -> QueryResponse.<ResponseLesson, Void>left(found.response())).orElse(QueryResponse.neither("Could not find lesson with id " + id));
    }
}
