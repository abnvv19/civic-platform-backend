package in.abnv.civic_platform_backend.civicproblems.entity;


import in.abnv.civic_platform_backend.ai.model.Severity;
import in.abnv.civic_platform_backend.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "civic_problems")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CivicProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProblemCategory category;

    @Enumerated(EnumType.STRING)
    private ProblemStatus status;

    private String address;

    private String city;

    @Column
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    private Boolean imageVerified;

    private Double aiConfidence;

    @Enumerated(EnumType.STRING)
    private ProblemCategory aiSuggestedCategory;

    @ManyToOne
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = ProblemStatus.OPEN;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
