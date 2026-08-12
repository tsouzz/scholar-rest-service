package io.github.thuliosouza.scholar_rest_service.domain.activity;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GradeService {

    public BigDecimal calculateGrade(List<Activity> activities) {
        return activities.stream()
                .map(a -> a.getGrade()
                        .divide(BigDecimal.TEN)
                        .multiply(a.getActivityType().getWeight())
                        .multiply(BigDecimal.valueOf(100)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateMaxPossible(List<Activity> activities) {
        return activities.stream()
                .map(a -> a.getActivityType().getWeight()
                        .multiply(BigDecimal.valueOf(100)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
