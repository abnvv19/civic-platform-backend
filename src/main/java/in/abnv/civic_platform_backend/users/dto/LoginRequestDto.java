package in.abnv.civic_platform_backend.users.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    private String email;

    private String password;
}
