package in.abnv.civic_platform_backend.civicproblems.dto;

import in.abnv.civic_platform_backend.ai.model.Severity;
import in.abnv.civic_platform_backend.civicproblems.entity.ProblemCategory;
import in.abnv.civic_platform_backend.civicproblems.entity.ProblemStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CivicProblemResponseDto {


    private Long id;

    private String title;

    private String description;

    private ProblemCategory category;

    private ProblemStatus status;

    private String address;

    private String city;

    private String imageUrl;

    private ProblemCategory aiSuggestedCategory;

    private Severity severity;

    private Boolean imageVerified;

    private Double aiConfidence;

    private Long reportedById;

    private String reportedByName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean duplicate;

    private Long duplicateProblemId;

    private Double duplicateConfidence;

    private String duplicateReason;
}
