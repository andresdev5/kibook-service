package ec.edu.espe.kibook.service.impl;

import ec.edu.espe.kibook.dto.AuthCredentialsDto;
import ec.edu.espe.kibook.dto.AuthResponseDto;
import ec.edu.espe.kibook.dto.UserDto;
import ec.edu.espe.kibook.dto.UserProfileDto;
import ec.edu.espe.kibook.entity.Role;
import ec.edu.espe.kibook.entity.User;
import ec.edu.espe.kibook.entity.UserProfile;
import ec.edu.espe.kibook.repository.RoleRepository;
import ec.edu.espe.kibook.repository.UserProfileRepository;
import ec.edu.espe.kibook.repository.UserRepository;
import ec.edu.espe.kibook.service.AuthService;
import ec.edu.espe.kibook.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponseDto login(AuthCredentialsDto credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        return AuthResponseDto.builder()
                .token(token)
                .user(user)
                .status(AuthResponseDto.ResponseStatus.SUCCESS)
                .code(200)
                .build();
    }

    @Override
    @Transactional
    public UserDto register(UserDto user) {
        userRepository.findFirstByUsernameIgnoreCase(user.getUsername())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "El nombre de usuario ya está registrado");
                });

        userRepository.findFirstByEmailIgnoreCase(user.getEmail())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "El correo electrónico ya está registrado");
                });

        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Las contraseñas no coinciden");
        }

        Optional<Role> optionalUserRole = roleRepository.findFirstByNameIgnoreCase("USER");
        Role userRole = optionalUserRole.orElseGet(()
                -> roleRepository.save(Role.builder().name("USER").description("Rol para usuarios regulares").build()));

        User newUser = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .role(userRole)
                .build();

        User savedUser = userRepository.save(newUser);
        UserProfile userProfile = UserProfile.builder()
                .user(savedUser)
                .build();

        UserProfile savedUserProfile = userProfileRepository.save(userProfile);

        return UserDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .profile(UserProfileDto.builder()
                        .id(savedUserProfile.getId())
                        .build())
                .build();
    }
}
