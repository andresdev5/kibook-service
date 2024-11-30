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
        Path booksPostersPath = Paths.get("uploads/books-posters");

        createDirectory(path);
        createDirectory(booksPostersPath);
    }

    private void createDirectory(Path path) {
        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path);
                logger.info("Carpeta '" + path + "' creada exitosamente.");
            } catch (Exception e) {
                logger.severe("Error al crear la carpeta '" + path + "': " + e.getMessage());
            }
        } else {
            logger.info("La carpeta '" + path + "' ya existe.");
        }
    }
}
