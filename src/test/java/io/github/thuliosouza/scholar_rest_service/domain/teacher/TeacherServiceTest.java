package io.github.thuliosouza.scholar_rest_service.domain.teacher;

import io.github.thuliosouza.scholar_rest_service.domain.school.School;
import io.github.thuliosouza.scholar_rest_service.domain.school.SchoolRepository;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.dto.TeacherRequest;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.dto.TeacherResponse;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.exception.TeacherNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private TeacherService teacherService;
    private Teacher teacher;
    private School school;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherRepository, schoolRepository, passwordEncoder);

        school = School.builder()
                .id(UUID.randomUUID())
                .name("CNA")
                .build();

        teacher = Teacher.builder()
                .id(UUID.randomUUID())
                .name("Thulio Souza")
                .email("thulio@cna.com")
                .passwordHash("hash")
                .school(school)
                .build();
    }

    @Test
    void findByIdShouldReturnTeacherResponse() {
        when(teacherRepository.findById(teacher.getId()))
                .thenReturn(Optional.of(teacher));

        TeacherResponse result = teacherService.findById(teacher.getId());

        assertNotNull(result);
        assertEquals(teacher.getId(), result.id());
        assertEquals(teacher.getName(), result.name());
        assertEquals(teacher.getEmail(), result.email());
    }

    @Test
    void findByIdShouldThrowWhenTeacherNotFound() {
        UUID id = UUID.randomUUID();

        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TeacherNotFoundException.class, () ->
                teacherService.findById(id)
        );
    }

    @Test
    void updateShouldChangeNameAndEmail() {
        TeacherRequest request = new TeacherRequest(
                "Novo Nome", "novo@cna.com", null, null, null
        );

        when(teacherRepository.findById(teacher.getId()))
                .thenReturn(Optional.of(teacher));
        when(teacherRepository.save(any())).thenReturn(teacher);

        TeacherResponse result = teacherService.update(teacher.getId(), request);

        assertNotNull(result);
        verify(teacherRepository, times(1)).save(teacher);
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void updateShouldHashPasswordWhenProvided() {
        TeacherRequest request = new TeacherRequest(
                "Thulio", "thulio@cna.com", "NovaSenha1!", "NovaSenha1!", null
        );

        when(teacherRepository.findById(teacher.getId()))
                .thenReturn(Optional.of(teacher));
        when(teacherRepository.save(any())).thenReturn(teacher);
        when(passwordEncoder.encode("NovaSenha1!")).thenReturn("novo_hash");

        teacherService.update(teacher.getId(), request);

        verify(passwordEncoder, times(1)).encode("NovaSenha1!");
    }

    @Test
    void updateShouldCreateNewSchoolWhenNotExists() {
        TeacherRequest request = new TeacherRequest(
                "Thulio", "thulio@cna.com", null, null, "Nova Escola"
        );

        School newSchool = School.builder()
                .id(UUID.randomUUID())
                .name("Nova Escola")
                .build();

        when(teacherRepository.findById(teacher.getId()))
                .thenReturn(Optional.of(teacher));
        when(schoolRepository.findSchoolByName("Nova Escola"))
                .thenReturn(Optional.empty());
        when(schoolRepository.save(any())).thenReturn(newSchool);
        when(teacherRepository.save(any())).thenReturn(teacher);

        teacherService.update(teacher.getId(), request);

        verify(schoolRepository, times(1)).save(any());
    }

    @Test
    void updateShouldReuseExistingSchool() {
        TeacherRequest request = new TeacherRequest(
                "Thulio", "thulio@cna.com", null, null, "CNA"
        );

        when(teacherRepository.findById(teacher.getId()))
                .thenReturn(Optional.of(teacher));
        when(schoolRepository.findSchoolByName("CNA"))
                .thenReturn(Optional.of(school));
        when(teacherRepository.save(any())).thenReturn(teacher);

        teacherService.update(teacher.getId(), request);

        verify(schoolRepository, never()).save(any());
    }

    @Test
    void deleteShouldThrowWhenTeacherNotFound() {
        UUID id = UUID.randomUUID();

        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TeacherNotFoundException.class, () ->
                teacherService.delete(id)
        );

        verify(teacherRepository, never()).delete(any());
    }

    @Test
    void deleteShouldRemoveTeacher() {
        when(teacherRepository.findById(teacher.getId()))
                .thenReturn(Optional.of(teacher));

        teacherService.delete(teacher.getId());

        verify(teacherRepository, times(1)).delete(teacher);
    }
}