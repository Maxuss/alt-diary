package space.maxus.dnevnik.data.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.model.Attachment;
import space.maxus.dnevnik.data.repo.AttachmentRepository;

@Service
public class AttachmentService extends AbstractCrudService<Attachment, Long, AttachmentRepository> {
    @Getter
    private final AttachmentRepository repository;

    public AttachmentService(AttachmentRepository repository) {
        this.repository = repository;
    }
}
