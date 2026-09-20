package io.github.thuliosouza.scholar_rest_service.domain.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeServiceTest {

    private GradeService gradeService;

    @BeforeEach
    void setUp() {
        gradeService = new GradeService();
    }

    private Activity activity(ActivityType type, String grade) {
        return Activity.builder()
                .activityType(type)
                .grade(new BigDecimal(grade))
                .registrationDate(LocalDate.now())
                .build();
    }

    @Test
    void calculateGradeShouldReturnZeroWhenNoActivities() {
        BigDecimal result = gradeService.calculateGrade(Collections.emptyList());
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateGradeShouldReturnHundredWhenAllActivitiesAreMax() {
        List<Activity> activities = List.of(
                activity(ActivityType.AC_1,     "10"),
                activity(ActivityType.AC_2,     "10"),
                activity(ActivityType.LC_1,     "10"),
                activity(ActivityType.LC_2,     "10"),
                activity(ActivityType.LC_3,     "10"),
                activity(ActivityType.LC_4,     "10"),
                activity(ActivityType.LC_5,     "10"),
                activity(ActivityType.LC_6,     "10"),
                activity(ActivityType.LC_7,     "10"),
                activity(ActivityType.LC_8,     "10"),
                activity(ActivityType.LW_1,     "10"),
                activity(ActivityType.LW_2,     "10"),
                activity(ActivityType.LW_3,     "10"),
                activity(ActivityType.LW_4,     "10"),
                activity(ActivityType.LW_5,     "10"),
                activity(ActivityType.LW_6,     "10"),
                activity(ActivityType.LW_7,     "10"),
                activity(ActivityType.LW_8,     "10"),
                activity(ActivityType.TO_MID,   "10"),
                activity(ActivityType.TO_FINAL, "10"),
                activity(ActivityType.TE_MID,   "10"),
                activity(ActivityType.TE_FINAL, "10")
        );

        BigDecimal result = gradeService.calculateGrade(activities);
        assertEquals(new BigDecimal("100.00"), result.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void calculateGradeShouldReturnPartialGrade() {
        List<Activity> activities = List.of(
                activity(ActivityType.TO_MID, "10")
        );

        BigDecimal result = gradeService.calculateGrade(activities);
        assertEquals(new BigDecimal("10.00"), result.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void calculateMaxPossibleShouldReturnZeroWhenNoActivities() {
        BigDecimal result = gradeService.calculateMaxPossible(Collections.emptyList());
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculateMaxPossibleShouldReturnHundredWhenAllActivities() {
        List<Activity> activities = List.of(
                activity(ActivityType.AC_1,     "0"),
                activity(ActivityType.AC_2,     "0"),
                activity(ActivityType.LC_1,     "0"),
                activity(ActivityType.LC_2,     "0"),
                activity(ActivityType.LC_3,     "0"),
                activity(ActivityType.LC_4,     "0"),
                activity(ActivityType.LC_5,     "0"),
                activity(ActivityType.LC_6,     "0"),
                activity(ActivityType.LC_7,     "0"),
                activity(ActivityType.LC_8,     "0"),
                activity(ActivityType.LW_1,     "0"),
                activity(ActivityType.LW_2,     "0"),
                activity(ActivityType.LW_3,     "0"),
                activity(ActivityType.LW_4,     "0"),
                activity(ActivityType.LW_5,     "0"),
                activity(ActivityType.LW_6,     "0"),
                activity(ActivityType.LW_7,     "0"),
                activity(ActivityType.LW_8,     "0"),
                activity(ActivityType.TO_MID,   "0"),
                activity(ActivityType.TO_FINAL, "0"),
                activity(ActivityType.TE_MID,   "0"),
                activity(ActivityType.TE_FINAL, "0")
        );

        BigDecimal result = gradeService.calculateMaxPossible(activities);
        assertEquals(new BigDecimal("100.00"), result.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void calculateMaxPossibleShouldReturnPartial() {
        List<Activity> activities = List.of(
                activity(ActivityType.TE_MID,   "0"),
                activity(ActivityType.TE_FINAL, "0")
        );

        BigDecimal result = gradeService.calculateMaxPossible(activities);
        assertEquals(new BigDecimal("40.00"), result.setScale(2, RoundingMode.HALF_UP));
    }
}