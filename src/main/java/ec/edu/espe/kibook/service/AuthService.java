package ec.edu.espe.kibook.service;

import ec.edu.espe.kibook.dto.AuthCredentialsDto;
import ec.edu.espe.kibook.dto.AuthResponseDto;
import ec.edu.espe.kibook.dto.UserDto;

public interface AuthService {
    AuthResponseDto login(AuthCredentialsDto credentials);
    UserDto register(UserDto user);
}
