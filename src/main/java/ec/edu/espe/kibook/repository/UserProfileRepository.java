package ec.edu.espe.kibook.repository;

import ec.edu.espe.kibook.entity.UserProfile;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface UserProfileRepository extends ListCrudRepository<UserProfile, UUID> {}
