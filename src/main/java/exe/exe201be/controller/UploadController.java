package exe.exe201be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class UploadController {

    private final S3Client s3;

    @Value("${app.aws.s3.bucket}")          // <-- đọc từ application.yml
    private String bucket;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File rỗng"));
            }
            String ext = getExt(file.getOriginalFilename());
            String key = "uploads/" + UUID.randomUUID() + (ext.isBlank() ? "" : "." + ext);

            PutObjectRequest put = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3.putObject(put, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return ResponseEntity.ok(Map.of("key", key));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private static String getExt(String name) {
        if (name == null) return "";
        int i = name.lastIndexOf('.');
        return (i >= 0 && i < name.length()-1) ? name.substring(i+1) : "";
    }
}
