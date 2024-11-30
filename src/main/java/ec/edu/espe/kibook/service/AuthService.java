package ec.edu.espe.kibook.service;

import ec.edu.espe.kibook.dto.AuthCredentialsDto;
import ec.edu.espe.kibook.dto.AuthResponseDto;

public interface AuthService {
    AuthResponseDto login(AuthCredentialsDto credentials);
}
