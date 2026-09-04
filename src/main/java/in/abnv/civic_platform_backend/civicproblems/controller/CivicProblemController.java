package in.abnv.civic_platform_backend.civicproblems.controller;


import in.abnv.civic_platform_backend.civicproblems.dto.CivicProblemResponseDto;
import in.abnv.civic_platform_backend.civicproblems.dto.CreateCivicProblemRequestDto;
import in.abnv.civic_platform_backend.civicproblems.service.CivicProblemService;
import in.abnv.civic_platform_backend.users.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class CivicProblemController {
    private final CivicProblemService civicProblemService;

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public CivicProblemResponseDto createProblem(
            @Valid @ModelAttribute CreateCivicProblemRequestDto requestDto,
            @RequestParam(required = false) MultipartFile image,
            Authentication authentication
    ) {
        System.out.println("CONTROLLER REACHED!");
        User user = (User) authentication.getPrincipal();

        return civicProblemService.createProblem(
                requestDto,
                image,
                user.getEmail()
        );
    }
    @GetMapping
    public List<CivicProblemResponseDto> getAllProblems() {

        return civicProblemService.getAllProblems();
    }
    @GetMapping("/{id}")
    public CivicProblemResponseDto getProblemById(
            @PathVariable Long id
    ) {
        return civicProblemService.getProblemById(id);
    }
    @GetMapping("/my-problems")
    public List<CivicProblemResponseDto> getMyProblems(
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        return civicProblemService.getMyProblems(
                user.getEmail()
        );
    }
}
