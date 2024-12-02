package ec.edu.espe.kibook.config;

import ec.edu.espe.kibook.entity.Permission;
import ec.edu.espe.kibook.entity.Role;
import ec.edu.espe.kibook.entity.User;
import ec.edu.espe.kibook.entity.UserProfile;
import ec.edu.espe.kibook.repository.PermissionRepository;
import ec.edu.espe.kibook.repository.RoleRepository;
import ec.edu.espe.kibook.repository.UserProfileRepository;
import ec.edu.espe.kibook.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class ConfigurationInitializer {
    private final Logger logger = Logger.getLogger(ConfigurationInitializer.class.getName());
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        createDirectories();
        createDefaultRoles();
        createDefaultPermissions();
        createAdminUser();
    }

    private void createDefaultRoles() {
        Role roleUser = roleRepository.findFirstByNameIgnoreCase("USER")
                .orElse(Role.builder().name("USER").description("Rol de usuario regular").build());
        Role roleAdmin = roleRepository.findFirstByNameIgnoreCase("ADMIN")
                .orElse(Role.builder().name("ADMIN").description("Rol de administrador").build());

        List<Role> roles = List.of(roleUser, roleAdmin);

        roles.forEach(role -> {
            boolean exists = role.getId() != null;

            if (!exists) {
                roleRepository.save(role);
                logger.info("Rol '" + role.getName() + "' creado exitosamente.");
            } else {
                logger.info("El rol '" + role.getName() + "' ya existe.");
            }
        });
    }

    private void createDefaultPermissions() {
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put("USER", List.of(
                "GET_BOOKS",
                "GET_BOOKS_SEARCH",
                "GET_GENRES",
                "GET_GENRES_SEARCH",
                "GET_AUTHORS",
                "GET_AUTHORS_SEARCH",
                "GET_USERS_ME"
        ));
        permissions.put("ADMIN", List.of(
                "GET_BOOKS",
                "GET_BOOKS_SEARCH",
                "POST_BOOKS",
                "PUT_BOOKS",
                "DELETE_BOOKS",
                "GET_GENRES",
                "GET_GENRES_SEARCH",
                "POST_GENRES",
                "PUT_GENRES",
                "DELETE_GENRES",
                "GET_AUTHORS",
                "GET_AUTHORS_SEARCH",
                "POST_AUTHORS",
                "PUT_AUTHORS",
                "DELETE_AUTHORS",
                "GET_USERS_ME"
        ));

        permissions.forEach((roleName, permissionList) -> {
            Optional<Role> role = roleRepository.findFirstByNameIgnoreCase(roleName);

            if (role.isPresent()) {
                Set<Permission> existingPermissions = role.get().getPermissions();
                Set<Permission> newPermissions = new HashSet<>();

                for (String permission : permissionList) {
                    boolean exists = existingPermissions.stream().anyMatch(p -> p.getName().equals(permission));

                    if (!exists) {
                        Permission newPermission = Permission.builder().name(permission).build();
                        newPermissions.add(newPermission);
                    }
                }

                List<Permission> addedPermissions = permissionRepository.saveAll(newPermissions);
                role.get().getPermissions().addAll(addedPermissions);
                roleRepository.save(role.get());

                logger.info(String.format("%d permisos añadidos al rol '%s'.", addedPermissions.size(), roleName));
            }
        });
    }

    private void createAdminUser() {
        if (userRepository.findByCredentialId("admin").isPresent()) {
            logger.info("El usuario administrador ya existe.");
            return;
        }

        User user = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("admin@admin.com")
                .role(roleRepository.findFirstByNameIgnoreCase("ADMIN").orElse(null))
                .build();
        User savedUser = userRepository.save(user);

        UserProfile profile = UserProfile.builder()
                .firstName("Admin")
                .lastName("Admin")
                .user(user)
                .build();

        logger.info("Usuario administrador creado exitosamente.");
    }

    private void createDirectories() {
        createDirectory(Paths.get("uploads"));
        createDirectory(Paths.get("uploads/books-posters"));
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
