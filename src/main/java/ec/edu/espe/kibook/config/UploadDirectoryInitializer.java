package ec.edu.espe.kibook.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Component
public class UploadDirectoryInitializer {
    private final Logger logger = Logger.getLogger(UploadDirectoryInitializer.class.getName());

    @PostConstruct
    public void init() {
        Path path = Paths.get("uploads");

        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path);
                logger.info("Carpeta 'uploads' creada exitosamente.");
            } catch (Exception e) {
                logger.severe("Error al crear la carpeta 'uploads': " + e.getMessage());
            }
        } else {
            logger.info("La carpeta 'uploads' ya existe.");
        }
    }
}
