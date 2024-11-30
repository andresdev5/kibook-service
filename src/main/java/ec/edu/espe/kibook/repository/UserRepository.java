package ec.edu.espe.kibook.repository;

import ec.edu.espe.kibook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findFirstByUsernameIgnoreCase(String username);
    Optional<User> findFirstByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);


    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:credentialId) OR LOWER(u.username) = LOWER(:credentialId)")
    Optional<User> findByCredentialId(String credentialId);
}
