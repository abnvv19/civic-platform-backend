package in.abnv.civic_platform_backend.civicproblems.repository;

import in.abnv.civic_platform_backend.civicproblems.entity.CivicProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CivicProblemRepository extends JpaRepository<CivicProblem, Long> {

    List<CivicProblem> findByReportedById(Long userId);
    List<CivicProblem> findByCity(String city);
}
