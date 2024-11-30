package ec.edu.espe.kibook.dto;

import ec.edu.espe.kibook.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {
    public enum ResponseStatus {
        SUCCESS,
        ERROR
    }

    private String token;
    private User user;
    private String message;
    private int code;
    private ResponseStatus status = ResponseStatus.SUCCESS;
}
