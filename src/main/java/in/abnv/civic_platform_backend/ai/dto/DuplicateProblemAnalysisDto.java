package in.abnv.civic_platform_backend.ai.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateProblemAnalysisDto {


    private Boolean duplicate;

    private Long duplicateProblemId;

    private Double confidence;

    private String reason;
}
