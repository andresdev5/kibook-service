package ec.edu.espe.kibook.repository;

import ec.edu.espe.kibook.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findFirstByNameIgnoreCase(String name);
}
