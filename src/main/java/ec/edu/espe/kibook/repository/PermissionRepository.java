package ec.edu.espe.kibook.repository;

import ec.edu.espe.kibook.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {}
