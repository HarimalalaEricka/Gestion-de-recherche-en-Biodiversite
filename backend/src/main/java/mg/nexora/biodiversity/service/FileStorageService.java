package mg.nexora.biodiversity.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Service
public class FileStorageService {

    private final MinioClient client;

    @Value("${minio.bucket}") private String bucket;
    @Value("${minio.public-url}") private String publicUrl;

    public FileStorageService(MinioClient client) {
        this.client = client;
    }

    /** Stocke le fichier et renvoie le nom de l'objet dans le bucket. */
    public String store(MultipartFile file, String folder) {
        try {
            String original = file.getOriginalFilename();
            String ext = (original != null && original.contains("."))
                    ? original.substring(original.lastIndexOf('.')).toLowerCase() : "";
            String objectName = folder + "/" + UUID.randomUUID() + ext;

            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("Échec de l'enregistrement du fichier", e);
        }
    }

    public String publicUrl(String objectName) {
        return publicUrl + "/" + bucket + "/" + objectName;
    }

    public void delete(String objectName) {
        try {
            client.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
        } catch (Exception ignored) {
            // nettoyage au mieux : on ne masque pas l'erreur d'origine
        }
    }
}