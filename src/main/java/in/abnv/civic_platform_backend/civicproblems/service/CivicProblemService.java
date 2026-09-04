package in.abnv.civic_platform_backend.civicproblems.service;


import in.abnv.civic_platform_backend.ai.dto.AiAnalysisResponseDto;
import in.abnv.civic_platform_backend.ai.dto.DuplicateProblemAnalysisDto;
import in.abnv.civic_platform_backend.ai.service.GeminiService;
import in.abnv.civic_platform_backend.civicproblems.dto.CivicProblemResponseDto;
import in.abnv.civic_platform_backend.civicproblems.dto.CreateCivicProblemRequestDto;
import in.abnv.civic_platform_backend.civicproblems.entity.CivicProblem;
import in.abnv.civic_platform_backend.civicproblems.repository.CivicProblemRepository;
import in.abnv.civic_platform_backend.storage.FileStorageService;
import in.abnv.civic_platform_backend.users.entity.User;
import in.abnv.civic_platform_backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CivicProblemService {

    private final CivicProblemRepository civicProblemRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final GeminiService geminiService;

    public CivicProblemResponseDto createProblem(
            CreateCivicProblemRequestDto requestDto,
            MultipartFile image,
            String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // 1. Find existing problems in the same city
        List<CivicProblem> existingProblems =
                civicProblemRepository.findByCity(requestDto.getCity());

        // 2. Check for duplicate using AI
        DuplicateProblemAnalysisDto duplicateAnalysis =
                geminiService.detectDuplicateProblem(
                        requestDto.getTitle(),
                        requestDto.getDescription(),
                        requestDto.getAddress(),
                        requestDto.getCity(),
                        existingProblems
                );

        // 3. Analyze problem using AI
        AiAnalysisResponseDto aiAnalysis =
                geminiService.analyzeProblem(
                        requestDto.getTitle(),
                        requestDto.getDescription(),
                        requestDto.getCategory(),
                        image
                );

        // 4. Save image locally
        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageService.saveFile(image);
        }

        // 5. Create the civic problem
        CivicProblem problem = CivicProblem.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .category(requestDto.getCategory())
                .address(requestDto.getAddress())
                .city(requestDto.getCity())
                .imageUrl(imageUrl)

                // AI analysis
                .severity(aiAnalysis.getSeverity())
                .imageVerified(aiAnalysis.getImageVerified())
                .aiConfidence(aiAnalysis.getConfidence())
                .aiSuggestedCategory(aiAnalysis.getSuggestedCategory())

                .reportedBy(user)
                .build();

        CivicProblem savedProblem =
                civicProblemRepository.save(problem);

        CivicProblemResponseDto response =
                mapToResponse(savedProblem);

        response.setDuplicate(
                duplicateAnalysis.getDuplicate()
        );

        response.setDuplicateProblemId(
                duplicateAnalysis.getDuplicateProblemId()
        );

        response.setDuplicateConfidence(
                duplicateAnalysis.getConfidence()
        );

        response.setDuplicateReason(
                duplicateAnalysis.getReason()
        );

        return response;
    }

    public List<CivicProblemResponseDto> getAllProblems() {

        List<CivicProblem> problems = civicProblemRepository.findAll();

        return problems.stream()
                .map(this::mapToResponse)
                .toList();
    }
    public CivicProblemResponseDto getProblemById(Long id) {

        CivicProblem problem = civicProblemRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Problem not found")
                );

        return mapToResponse(problem);
    }
    public List<CivicProblemResponseDto> getMyProblems(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        List<CivicProblem> problems =
                civicProblemRepository.findByReportedById(user.getId());

        return problems.stream()
                .map(this::mapToResponse)
                .toList();
    }
    private CivicProblemResponseDto mapToResponse(
            CivicProblem problem
    ) {

        return CivicProblemResponseDto.builder()
                .id(problem.getId())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .category(problem.getCategory())
                .status(problem.getStatus())
                .address(problem.getAddress())
                .city(problem.getCity())
                .imageUrl(problem.getImageUrl())
                .aiSuggestedCategory(problem.getAiSuggestedCategory())
                .severity(problem.getSeverity())
                .imageVerified(problem.getImageVerified())
                .aiConfidence(problem.getAiConfidence())
                .reportedById(problem.getReportedBy().getId())
                .reportedByName(problem.getReportedBy().getName())
                .createdAt(problem.getCreatedAt())
                .updatedAt(problem.getUpdatedAt())
                .build();
    }

}
