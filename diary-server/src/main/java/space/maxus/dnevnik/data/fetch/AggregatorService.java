package space.maxus.dnevnik.data.fetch;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.service.TeacherService;

@Service @Getter
public class AggregatorService implements InitializingBean {
    public static AggregatorService INSTANCE = null;
    private final TeacherService teacherService;

    public AggregatorService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    public void afterPropertiesSet() {
        if(INSTANCE == null)
            AggregatorService.INSTANCE = this;
    }
}
