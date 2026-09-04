package in.abnv.civic_platform_backend.ai.service;


import in.abnv.civic_platform_backend.ai.dto.AiAnalysisResponseDto;
import in.abnv.civic_platform_backend.ai.dto.DuplicateProblemAnalysisDto;
import in.abnv.civic_platform_backend.civicproblems.entity.CivicProblem;
import in.abnv.civic_platform_backend.civicproblems.entity.ProblemCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.util.Base64;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper;

    private final RestClient restClient = RestClient.create();

    public AiAnalysisResponseDto analyzeProblem(
            String title,
            String description,
            ProblemCategory selectedCategory,
            MultipartFile image
    ) {

        String prompt = """
                You are an AI system for a civic problem reporting platform.

                Analyze the civic problem using the provided title,
                description, selected category, and image.

                Return your analysis in JSON format.

                Allowed categories:
                ROAD_DAMAGE,
                GARBAGE,
                STREETLIGHT,
                WATER_LEAKAGE,
                SANITATION,
                ELECTRICITY_SHORTAGE,
                AGRICULTURE,
                HEALTHCARE,
                OTHER

                Allowed severity values:
                LOW,
                MEDIUM,
                HIGH,
                CRITICAL

                Problem details:

                Title: %s
                Description: %s
                User selected category: %s

                Determine:

                1. suggestedCategory
                2. severity
                3. imageVerified - true if the image appears related
                   to the reported civic problem
                4. confidence - number between 0 and 100
                """.formatted(
                title,
                description,
                selectedCategory
        );

        try {

            String base64Image = null;
            String mimeType = null;

            if (image != null && !image.isEmpty()) {
                base64Image = Base64.getEncoder()
                        .encodeToString(image.getBytes());

                mimeType = image.getContentType();
            }

            Map<String, Object> textPart = Map.of(
                    "text", prompt
            );

            Object requestBody;

            if (base64Image != null) {

                Map<String, Object> imagePart = Map.of(
                        "inlineData", Map.of(
                                "mimeType", mimeType,
                                "data", base64Image
                        )
                );

                requestBody = Map.of(
                        "contents", new Object[]{
                                Map.of(
                                        "parts", new Object[]{
                                                textPart,
                                                imagePart
                                        }
                                )
                        },
                        "generationConfig", Map.of(
                                "responseMimeType", "application/json"
                        )
                );

            } else {

                requestBody = Map.of(
                        "contents", new Object[]{
                                Map.of(
                                        "parts", new Object[]{
                                                textPart
                                        }
                                )
                        },
                        "generationConfig", Map.of(
                                "responseMimeType", "application/json"
                        )
                );
            }

            String response = restClient.post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent")
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);

            String aiText = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            return objectMapper.readValue(
                    aiText,
                    AiAnalysisResponseDto.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "AI analysis failed",
                    e
            );
        }
    }
    public DuplicateProblemAnalysisDto detectDuplicateProblem(
            String title,
            String description,
            String address,
            String city,
            List<CivicProblem> existingProblems
    ) {

        if (existingProblems == null || existingProblems.isEmpty()) {
            DuplicateProblemAnalysisDto result =
                    new DuplicateProblemAnalysisDto();

            result.setDuplicate(false);
            result.setDuplicateProblemId(null);
            result.setConfidence(0.0);
            result.setReason("No existing problems found in this city.");

            return result;
        }

        StringBuilder existingProblemsText = new StringBuilder();

        for (CivicProblem problem : existingProblems) {

            existingProblemsText.append("""
                
                Problem ID: %d
                Title: %s
                Description: %s
                Category: %s
                Address: %s
                
                """.formatted(
                    problem.getId(),
                    problem.getTitle(),
                    problem.getDescription(),
                    problem.getCategory(),
                    problem.getAddress()
            ));
        }

        String prompt = """
            You are an AI duplicate detection system for a civic problem platform.

            Determine whether the NEW PROBLEM is describing the same
            real-world civic issue as one of the EXISTING PROBLEMS.

            Consider:
            - Title similarity
            - Description similarity
            - Category
            - Address/location

            NEW PROBLEM:

            Title: %s
            Description: %s
            Address: %s
            City: %s

            EXISTING PROBLEMS:

            %s

            Return ONLY valid JSON in this format:

            {
              "duplicate": true,
              "duplicateProblemId": 1,
              "confidence": 90.0,
              "reason": "Brief explanation"
            }

            If no duplicate exists:

            {
              "duplicate": false,
              "duplicateProblemId": null,
              "confidence": 0.0,
              "reason": "No similar problem found"
            }
            """.formatted(
                title,
                description,
                address,
                city,
                existingProblemsText
        );

        try {

            Map<String, Object> requestBody = Map.of(
                    "contents", new Object[]{
                            Map.of(
                                    "parts", new Object[]{
                                            Map.of("text", prompt)
                                    }
                            )
                    },
                    "generationConfig", Map.of(
                            "responseMimeType", "application/json"
                    )
            );

            String response = restClient.post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent")
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);

            String aiText = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            return objectMapper.readValue(
                    aiText,
                    DuplicateProblemAnalysisDto.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Duplicate problem detection failed",
                    e
            );
        }
    }

}
