package io.github.thuliosouza.scholar_rest_service.domain.activity;

import io.github.thuliosouza.scholar_rest_service.domain.activity.exception.ActivityNotFoundException;
import io.github.thuliosouza.scholar_rest_service.domain.activity.exception.AlreadyRegisteredActivityException;
import io.github.thuliosouza.scholar_rest_service.domain.activity.exception.InvalidGradeException;
import io.github.thuliosouza.scholar_rest_service.domain.student.Student;
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

    @Transactional
    public Activity createActivity(
            ActivityType type,
            BigDecimal grade,
            Student student
    ){
        Activity existing = activityRepository.findByStudentIdAndActivityType(student.getId(), type);
        if (existing != null) {
            throw new AlreadyRegisteredActivityException(type.getLabel() + " já registrada para esse aluno!");
        }

        Activity activity = Activity.builder()
                .grade(grade)
                .registrationDate(LocalDate.now())
                .activityType(type)
                .student(student)
                .build();

        return activityRepository.save(activity);
    }

    public List<Activity> findByStudent(UUID studentId){
        return activityRepository.findAllByStudentId(studentId);
    }

    public Activity findByStudentAndType(UUID studentId, ActivityType activityType){
        return activityRepository.findByStudentIdAndActivityType(studentId, activityType);
    }

    @Transactional
    public Activity update(UUID id, BigDecimal grade) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException("Atividade não encontrada!"));
        activity.setGrade(grade);

        return activity;
    }

    @Transactional
    public void delete(UUID id){
        activityRepository.deleteById(id);
    }
}
