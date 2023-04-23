package space.maxus.dnevnik.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.StandardException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
    private final Path basePath;

    public FileService(DiaryConfiguration config, ResourceLoader resourceLoader) {
        this.basePath = Paths.get(config.getFiles().getPath());
        if(!basePath.toFile().exists() && (!basePath.toFile().mkdirs())) {
            throw new FileServiceInitException("Failed to init FileService: base path does not exist and could not be created");
        }
    }

    public Path saveFile(MultipartFile file, String name) throws IOException {
        String origName = name.replaceAll("[\\\\/~^]|(\\.\\.)", "").replace(' ', '_');
        String resultName = origName;
        int idx = 1;
        while(Files.exists(basePath.resolve(resultName))) {
            resultName = "(%s) %s".formatted(idx++, origName);
        }
        Path last = basePath.resolve(resultName);
        Files.copy(file.getInputStream(), last);
        return last;
    }

    public File loadFile(String name) {
        Path path = basePath.resolve(name);
        return path.toFile();
    }

    public ResponseEntity<InputStreamResource> loadFileResponse(HttpServletResponse response, String name) throws IOException {
        File file = loadFile(name);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(file.toPath())))
                .contentLength(file.length())
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    @StandardException
    private static class FileServiceInitException extends RuntimeException {

    }
}
