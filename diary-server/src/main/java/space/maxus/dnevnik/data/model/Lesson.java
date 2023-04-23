package space.maxus.dnevnik.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import space.maxus.dnevnik.controllers.response.ResponseLesson;
import space.maxus.dnevnik.data.fetch.AggregatorService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class Lesson {
    private final long subjectId;
    private final long homeworkId;
    private final LocalTime beginTime;
    private final LocalTime endTime;

    @Column(name = "marks")
    private long[] markIds = new long[0];
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "lessons_id_seq")
    private Long id;
    @OneToMany(mappedBy = "lesson")
    private List<Mark> marks = new ArrayList<>();

    public Optional<Homework> getHomework() {
        return AggregatorService.INSTANCE.getHomeworkService().findById(homeworkId);
    }

    public ResponseLesson response(long nextBreak) {
        return new ResponseLesson(
                id,
                beginTime,
                endTime,
                nextBreak,
                AggregatorService.INSTANCE.getSubjectService().findById(subjectId).map(Subject::response).orElse(null),
                marks.stream().map(Mark::response).toList(),
                AggregatorService.INSTANCE.getHomeworkService().findById(homeworkId).map(Homework::response).orElse(null)
        );
    }

    public Lesson withAdditionalMark(long markId) {
        long[] newMarks = new long[markIds.length + 1];
        System.arraycopy(markIds, 0, newMarks, 0, markIds.length);
        newMarks[markIds.length] = markId;
        return new Lesson(
                this.subjectId,
                this.homeworkId,
                this.beginTime,
                this.endTime,
                newMarks,
                this.id,
                this.marks
        );
    }

    public Lesson withHomework(long homeworkId) {
        return new Lesson(
                this.subjectId,
                this.homeworkId,
                this.beginTime,
                this.endTime,
                this.markIds,
                this.id,
                this.marks
        );
    }
}
