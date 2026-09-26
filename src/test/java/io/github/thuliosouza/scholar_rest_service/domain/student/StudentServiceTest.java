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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ClassGroupRepository classGroupRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private GradeService gradeService;

    private StudentService studentService;
    private ClassGroup classGroupA;
    private ClassGroup classGroupB;
    private Student student;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(
                studentRepository, classGroupRepository, activityRepository, gradeService
        );

        classGroupA = ClassGroup.builder()
                .id(UUID.randomUUID())
                .number(1)
                .module(io.github.thuliosouza.scholar_rest_service.domain.classgroup.Module.INTERMEDIATE_1)
                .build();

        classGroupB = ClassGroup.builder()
                .id(UUID.randomUUID())
                .number(1)
                .module(io.github.thuliosouza.scholar_rest_service.domain.classgroup.Module.INTERMEDIATE_1)
                .build();

        student = Student.builder()
                .id(UUID.randomUUID())
                .name("Aluno Teste")
                .classGroup(classGroupA)
                .build();
    }

    @Test
    void createStudentShouldThrowWhenClassGroupNotFound() {
        StudentRequest request = new StudentRequest("Aluno Teste", UUID.randomUUID());

        when(classGroupRepository.findById(request.classGroupId()))
                .thenReturn(Optional.empty());

        assertThrows(ClassGroupNotFoundException.class, () ->
                studentService.createStudent(request)
        );

        verify(studentRepository, never()).save(any());
    }

    @Test
    void createStudentShouldSucceedWithValidData() {
        StudentRequest request = new StudentRequest("Aluno Teste", classGroupA.getId());

        when(classGroupRepository.findById(classGroupA.getId()))
                .thenReturn(Optional.of(classGroupA));
        when(studentRepository.save(any())).thenReturn(student);

        StudentResponse result = studentService.createStudent(request);

        assertNotNull(result);
        assertEquals("Aluno Teste", result.name());
        assertEquals(BigDecimal.ZERO, result.grade());
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    void findByIdShouldThrowWhenStudentNotFound() {
        UUID id = UUID.randomUUID();

        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () ->
                studentService.findById(id)
        );
    }

    @Test
    void transferStudentShouldSucceedWhenSameModule() {
        when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.of(student));
        when(classGroupRepository.findById(classGroupB.getId()))
                .thenReturn(Optional.of(classGroupB));
        when(studentRepository.save(any())).thenReturn(student);
        when(activityRepository.findAllByStudentId(student.getId()))
                .thenReturn(Collections.emptyList());
        when(gradeService.calculateGrade(any())).thenReturn(BigDecimal.ZERO);

        StudentResponse result = studentService.transferStudent(student.getId(), classGroupB.getId());

        assertNotNull(result);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void transferStudentShouldThrowWhenDifferentModule() {
        ClassGroup differentModule = ClassGroup.builder()
                .id(UUID.randomUUID())
                .number(1)
                .module(io.github.thuliosouza.scholar_rest_service.domain.classgroup.Module.ADVANCED_1)
                .build();

        when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.of(student));
        when(classGroupRepository.findById(differentModule.getId()))
                .thenReturn(Optional.of(differentModule));

        assertThrows(InvalidOperationException.class, () ->
                studentService.transferStudent(student.getId(), differentModule.getId())
        );

        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteShouldThrowWhenStudentNotFound() {
        UUID id = UUID.randomUUID();

        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () ->
                studentService.delete(id)
        );

        verify(studentRepository, never()).delete(any());
    }

    @Test
    void updateNameShouldPersistNewName() {
        when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.of(student));
        when(studentRepository.save(any())).thenReturn(student);
        when(activityRepository.findAllByStudentId(student.getId()))
                .thenReturn(Collections.emptyList());
        when(gradeService.calculateGrade(any())).thenReturn(BigDecimal.ZERO);

        StudentResponse result = studentService.updateName(student.getId(), "Novo Nome");

        assertNotNull(result);
        verify(studentRepository, times(1)).save(student);
    }
}