package io.github.thuliosouza.scholar_rest_service.domain.student;

import io.github.thuliosouza.scholar_rest_service.domain.activity.ActivityRepository;
import io.github.thuliosouza.scholar_rest_service.domain.activity.GradeService;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.ClassGroup;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.ClassGroupRepository;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.exception.ClassGroupNotFoundException;
import io.github.thuliosouza.scholar_rest_service.domain.student.dto.StudentRequest;
import io.github.thuliosouza.scholar_rest_service.domain.student.dto.StudentResponse;
import io.github.thuliosouza.scholar_rest_service.domain.student.exception.InvalidOperationException;
import io.github.thuliosouza.scholar_rest_service.domain.student.exception.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassGroupRepository classGroupRepository;
    private final ActivityRepository activityRepository;
    private final GradeService gradeService;

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        ClassGroup classGroup = classGroupRepository.findById(request.classGroupId())
                .orElseThrow(() -> new ClassGroupNotFoundException("Classe não encontrada!"));

        Student student = Student.builder()
                .name(request.name())
                .classGroup(classGroup)
                .build();

        Student saved = studentRepository.save(student);
        return StudentResponse.from(saved, BigDecimal.ZERO);
    }

    public StudentResponse findById(UUID studentId) {
        Student student = getStudentEntity(studentId);
        BigDecimal grade = gradeService.calculateGrade(
                activityRepository.findAllByStudentId(student.getId())
        );
        return StudentResponse.from(student, grade);
    }

    public List<StudentResponse> findByClassGroupId(UUID classGroupId) {
        return studentRepository.findByClassGroupId(classGroupId)
                .stream()
                .map(student -> StudentResponse.from(
                        student,
                        gradeService.calculateGrade(
                                activityRepository.findAllByStudentId(student.getId())
                        )
                ))
                .toList();
    }

    @Transactional
    public StudentResponse transferStudent(UUID studentId, UUID classGroupId) {
        Student student = getStudentEntity(studentId);

        ClassGroup classGroup = classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new ClassGroupNotFoundException("Turma não encontrada!"));

        if (student.getClassGroup().getModule() != classGroup.getModule()) {
            throw new InvalidOperationException(
                    "Transferência inválida: a turma destino pertence a um módulo diferente."
            );
        }

        student.setClassGroup(classGroup);
        Student saved = studentRepository.save(student);
        BigDecimal grade = gradeService.calculateGrade(
                activityRepository.findAllByStudentId(saved.getId())
        );
        return StudentResponse.from(saved, grade);
    }

    @Transactional
    public StudentResponse updateName(UUID studentId, String name) {
        Student student = getStudentEntity(studentId);
        student.setName(name);
        Student saved = studentRepository.save(student);
        BigDecimal grade = gradeService.calculateGrade(
                activityRepository.findAllByStudentId(saved.getId())
        );
        return StudentResponse.from(saved, grade);
    }

    @Transactional
    public void delete(UUID studentId) {
        studentRepository.delete(getStudentEntity(studentId));
    }

    private Student getStudentEntity(UUID studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado!"));

    }
}
