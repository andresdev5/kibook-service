package ec.edu.espe.kibook.service.impl;

import ec.edu.espe.kibook.dto.AuthCredentialsDto;
import ec.edu.espe.kibook.dto.AuthResponseDto;
import ec.edu.espe.kibook.entity.User;
import ec.edu.espe.kibook.service.AuthService;
import ec.edu.espe.kibook.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtService jwtService;

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
}
