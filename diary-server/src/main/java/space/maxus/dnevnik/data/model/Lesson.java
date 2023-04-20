package space.maxus.dnevnik.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import space.maxus.dnevnik.controllers.response.ResponseLesson;
import space.maxus.dnevnik.data.fetch.AggregatorService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "lessons")
@Data @NoArgsConstructor(force = true) @RequiredArgsConstructor @AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "lessons_id_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private final long subjectId;
    @Column(name = "marks")
    private final long[] markIds;

    private final long homeworkId;
    @Column(name = "day")
    private final Date date;

    @OneToMany(mappedBy = "lesson")
    private List<Mark> marks = new ArrayList<>();

    public Optional<Homework> getHomework() {
        return AggregatorService.INSTANCE.getHomeworkService().findById(homeworkId);
    }

    public ResponseLesson response() {
        return new ResponseLesson(
                id,
                AggregatorService.INSTANCE.getSubjectService().findById(subjectId).map(Subject::response).orElse(null),
                marks.stream().map(Mark::response).toList(),
                AggregatorService.INSTANCE.getHomeworkService().findById(homeworkId).map(Homework::response).orElse(null), date);
    }
}
