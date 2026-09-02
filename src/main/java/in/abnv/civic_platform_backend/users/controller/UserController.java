package in.abnv.civic_platform_backend.users.controller;


import in.abnv.civic_platform_backend.users.dto.LoginRequestDto;
import in.abnv.civic_platform_backend.users.dto.LoginResponseDto;
import in.abnv.civic_platform_backend.users.dto.RegisterUserRequestDto;
import in.abnv.civic_platform_backend.users.entity.User;
import in.abnv.civic_platform_backend.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterUserRequestDto requestDto) {

        User user = userService.registerUser(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }
    @PostMapping("/login")
    public LoginResponseDto loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.loginUser(loginRequestDto);
    }
    @GetMapping("/profile")
    public String getProfile() {
        return "You are authenticated!";
    }
}
