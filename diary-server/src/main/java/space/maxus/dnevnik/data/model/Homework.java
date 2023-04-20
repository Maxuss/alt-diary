package space.maxus.dnevnik.data.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import space.maxus.dnevnik.controllers.response.ResponseHomework;
import space.maxus.dnevnik.data.fetch.AggregatorService;

import java.util.*;

@Entity
@Table(name = "homeworks")
@Data @NoArgsConstructor(force = true) @RequiredArgsConstructor @AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "$id")
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "schedules_id_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private final boolean isNone;
    private final String summary;
    private final UUID teacherId;
    @Column(name = "attachments")
    private final long[] attachmentIds;

    public List<Attachment> getAttachments() {
        if(attachmentIds.length == 0)
            return Collections.emptyList();
        return Arrays.stream(ArrayUtils.toObject(attachmentIds)).map(each -> AggregatorService.INSTANCE.getAttachmentService().findById(each).orElse(null)).filter(Objects::nonNull).toList();
    }

    public ResponseHomework response() {
        return new ResponseHomework(id, isNone, summary, teacherId, getAttachments());
    }
}
