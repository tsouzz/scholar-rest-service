package io.github.thuliosouza.scholar_rest_service.domain.activity;

import io.github.thuliosouza.scholar_rest_service.domain.activity.dto.ActivityRequest;
import io.github.thuliosouza.scholar_rest_service.domain.activity.dto.ActivityResponse;
import io.github.thuliosouza.scholar_rest_service.domain.activity.exception.ActivityNotFoundException;
import io.github.thuliosouza.scholar_rest_service.domain.activity.exception.AlreadyRegisteredActivityException;
import io.github.thuliosouza.scholar_rest_service.domain.student.Student;
import io.github.thuliosouza.scholar_rest_service.domain.student.StudentRepository;
import io.github.thuliosouza.scholar_rest_service.domain.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private StudentRepository studentRepository;

    private ActivityService activityService;
    private Student student;

    @BeforeEach
    void setUp() {
        activityService = new ActivityService(activityRepository, studentRepository);
        student = Student.builder()
                .id(UUID.randomUUID())
                .name("Aluno Teste")
                .build();
    }

    @Test
    void createActivityShouldThrowWhenActivityAlreadyExists() {
        Activity existing = Activity.builder()
                .activityType(ActivityType.TO_MID)
                .grade(new BigDecimal("8.0"))
                .registrationDate(LocalDate.now())
                .student(student)
                .build();

        ActivityRequest request = new ActivityRequest(
                student.getId(), ActivityType.TO_MID, new BigDecimal("9.0")
        );

        when(activityRepository.findByStudentIdAndActivityType(student.getId(), ActivityType.TO_MID))
                .thenReturn(Optional.of(existing));

        assertThrows(AlreadyRegisteredActivityException.class, () ->
                activityService.createActivity(request)
        );

        verify(activityRepository, never()).save(any());
    }

    @Test
    void createActivityShouldThrowWhenStudentNotFound() {
        ActivityRequest request = new ActivityRequest(
                student.getId(), ActivityType.AC_1, new BigDecimal("8.0")
        );

        when(activityRepository.findByStudentIdAndActivityType(student.getId(), ActivityType.AC_1))
                .thenReturn(Optional.empty());

        when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () ->
                activityService.createActivity(request)
        );

        verify(activityRepository, never()).save(any());
    }

    @Test
    void createActivityShouldSucceedWithValidData() {
        ActivityRequest request = new ActivityRequest(
                student.getId(), ActivityType.AC_1, new BigDecimal("8.0")
        );

        Activity saved = Activity.builder()
                .id(UUID.randomUUID())
                .activityType(ActivityType.AC_1)
                .grade(new BigDecimal("8.0"))
                .registrationDate(LocalDate.now())
                .student(student)
                .build();

        when(activityRepository.findByStudentIdAndActivityType(student.getId(), ActivityType.AC_1))
                .thenReturn(Optional.empty());
        when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.of(student));
        when(activityRepository.save(any()))
                .thenReturn(saved);

        ActivityResponse result = activityService.createActivity(request);

        assertNotNull(result);
        assertEquals(ActivityType.AC_1, result.activityType());
        verify(activityRepository, times(1)).save(any());
    }

    @Test
    void findByIdShouldThrowWhenActivityNotFound() {
        UUID id = UUID.randomUUID();

        when(activityRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ActivityNotFoundException.class, () ->
                activityService.findById(id)
        );
    }

    @Test
    void updateShouldChangeGrade() {
        UUID id = UUID.randomUUID();
        Activity activity = Activity.builder()
                .id(id)
                .activityType(ActivityType.TE_MID)
                .grade(new BigDecimal("7.0"))
                .registrationDate(LocalDate.now())
                .student(student)
                .build();

        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        when(activityRepository.save(any())).thenReturn(activity);

        ActivityResponse result = activityService.update(id, new BigDecimal("9.0"));

        assertEquals(new BigDecimal("9.0"), result.grade());
        verify(activityRepository, times(1)).save(activity);
    }

    @Test
    void deleteShouldThrowWhenActivityNotFound() {
        UUID id = UUID.randomUUID();

        when(activityRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ActivityNotFoundException.class, () ->
                activityService.delete(id)
        );

        verify(activityRepository, never()).delete(any());
    }
}