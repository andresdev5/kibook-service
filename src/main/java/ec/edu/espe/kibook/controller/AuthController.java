package ec.edu.espe.kibook.controller;

import ec.edu.espe.kibook.dto.AuthCredentialsDto;
import ec.edu.espe.kibook.dto.AuthResponseDto;
import ec.edu.espe.kibook.dto.UserDto;
import ec.edu.espe.kibook.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthCredentialsDto credentials) {
        return ResponseEntity.ok(authService.login(credentials));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Validated({ UserDto.Create.class }) UserDto userDto) {
        return ResponseEntity.ok(authService.register(userDto));
    }
}
