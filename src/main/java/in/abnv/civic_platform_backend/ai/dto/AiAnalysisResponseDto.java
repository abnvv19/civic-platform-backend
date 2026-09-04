package in.abnv.civic_platform_backend.ai.dto;

import in.abnv.civic_platform_backend.ai.model.Severity;
import in.abnv.civic_platform_backend.civicproblems.entity.ProblemCategory;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AiAnalysisResponseDto {

    private ProblemCategory suggestedCategory;

    private Severity severity;

    private Boolean imageVerified;

    private Double confidence;
}
