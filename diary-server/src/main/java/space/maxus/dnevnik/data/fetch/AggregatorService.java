package space.maxus.dnevnik.data.fetch;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.service.StudentService;
import space.maxus.dnevnik.data.service.TeacherService;

@Service @Getter
public class AggregatorService implements InitializingBean {
    public static AggregatorService INSTANCE = null;
    private final TeacherService teacherService;
    private final StudentService studentService;

    public AggregatorService(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @Override
    public void afterPropertiesSet() {
        if(INSTANCE == null)
            AggregatorService.INSTANCE = this;
    }
}
