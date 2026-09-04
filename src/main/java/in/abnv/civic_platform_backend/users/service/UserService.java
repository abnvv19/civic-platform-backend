package in.abnv.civic_platform_backend.users.service;

import in.abnv.civic_platform_backend.exception.InvalidPasswordException;
import in.abnv.civic_platform_backend.exception.UserNotFoundException;
import in.abnv.civic_platform_backend.jwt.JwtService;
import in.abnv.civic_platform_backend.users.dto.LoginRequestDto;
import in.abnv.civic_platform_backend.users.dto.LoginResponseDto;
import in.abnv.civic_platform_backend.users.dto.RegisterUserRequestDto;
import in.abnv.civic_platform_backend.users.entity.Role;
import in.abnv.civic_platform_backend.users.entity.User;
import in.abnv.civic_platform_backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public User registerUser(RegisterUserRequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (requestDto.getRole() == Role.ADMIN) {
            throw new RuntimeException("You cannot register as ADMIN");
        }

        User user = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(requestDto.getRole())
                .build();

        return userRepository.save(user);
    }

    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(
                loginRequestDto.getPassword(),
                user.getPassword()
        )) {
            throw new InvalidPasswordException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return LoginResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }


}
