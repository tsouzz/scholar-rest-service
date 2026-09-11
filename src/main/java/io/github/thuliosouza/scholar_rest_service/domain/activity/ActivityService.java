package io.github.thuliosouza.scholar_rest_service.domain.activity;

import io.github.thuliosouza.scholar_rest_service.domain.activity.dto.ActivityRequest;
import io.github.thuliosouza.scholar_rest_service.domain.activity.dto.ActivityResponse;
import io.github.thuliosouza.scholar_rest_service.domain.activity.exception.ActivityNotFoundException;
import io.github.thuliosouza.scholar_rest_service.domain.activity.exception.AlreadyRegisteredActivityException;
import io.github.thuliosouza.scholar_rest_service.domain.student.Student;
import io.github.thuliosouza.scholar_rest_service.domain.student.StudentRepository;
import io.github.thuliosouza.scholar_rest_service.domain.student.exception.StudentNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public ActivityResponse createActivity(ActivityRequest request) {
        activityRepository.findByStudentIdAndActivityType(request.studentId(), request.activityType())
                .ifPresent(a -> {
                    throw new AlreadyRegisteredActivityException(
                            request.activityType().getLabel() + " já registrada para esse aluno."
                    );
                });

        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado."));

        Activity activity = Activity.builder()
                .grade(request.grade())
                .registrationDate(LocalDate.now())
                .activityType(request.activityType())
                .student(student)
                .build();

        return ActivityResponse.from(activityRepository.save(activity));
    }

    public List<ActivityResponse> findByStudent(UUID studentId) {
        return activityRepository.findAllByStudentId(studentId)
                .stream()
                .map(ActivityResponse::from)
                .toList();
    }

    public ActivityResponse findById(UUID id) {
        return ActivityResponse.from(getActivityEntity(id));
    }

    @Transactional
    public ActivityResponse update(UUID id, BigDecimal grade) {
        Activity activity = getActivityEntity(id);
        activity.setGrade(grade);
        return ActivityResponse.from(activityRepository.save(activity));
    }

    @Transactional
    public void delete(UUID id) {
        activityRepository.delete(getActivityEntity(id));
    }

    private Activity getActivityEntity(UUID id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException("Atividade não encontrada."));
    }
}
