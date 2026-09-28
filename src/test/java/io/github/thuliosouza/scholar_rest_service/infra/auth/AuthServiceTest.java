package io.github.thuliosouza.scholar_rest_service.infra.auth;

import io.github.thuliosouza.scholar_rest_service.domain.school.School;
import io.github.thuliosouza.scholar_rest_service.domain.school.SchoolRepository;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.Teacher;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.TeacherRepository;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.dto.TeacherRequest;
import io.github.thuliosouza.scholar_rest_service.infra.auth.dto.LoginRequest;
import io.github.thuliosouza.scholar_rest_service.infra.auth.dto.TokenResponse;
import io.github.thuliosouza.scholar_rest_service.infra.security.TokenService;
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
class AuthServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;
    private Teacher teacher;
    private School school;

    @BeforeEach
    void setUp() {
        authService = new AuthService(teacherRepository, schoolRepository, tokenService, passwordEncoder);

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
    void registerShouldThrowWhenPasswordTooShort() {
        TeacherRequest request = new TeacherRequest(
                "Thulio", "thulio@cna.com", "Ab1", "Ab1", "CNA"
        );

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(request)
        );
    }

    @Test
    void registerShouldThrowWhenPasswordHasNoUppercase() {
        TeacherRequest request = new TeacherRequest(
                "Thulio", "thulio@cna.com", "abcdefg1", "abcdefg1", "CNA"
        );

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(request)
        );
    }

    @Test
    void registerShouldThrowWhenPasswordHasNoNumber() {
        TeacherRequest request = new TeacherRequest(
                "Thulio", "thulio@cna.com", "Abcdefgh", "Abcdefgh", "CNA"
        );

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(request)
        );
    }

    @Test
    void registerShouldThrowWhenPasswordsDoNotMatch() {
        TeacherRequest request = new TeacherRequest(
                "Thulio", "thulio@cna.com", "Abcdef1!", "Abcdef2!", "CNA"
        );

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(request)
        );
    }

    @Test
    void registerShouldThrowWhenEmailAlreadyExists() {
        TeacherRequest request = new TeacherRequest(
                "Thulio", "thulio@cna.com", "Abcdef1!", "Abcdef1!", "CNA"
        );

        when(teacherRepository.existsTeacherByEmail("thulio@cna.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(request)
        );

        verify(teacherRepository, never()).save(any());
    }

    @Test
    void registerShouldCreateSchoolWhenNotExists() {
        TeacherRequest request = new TeacherRequest(
                "Thulio", "thulio@cna.com", "Abcdef1!", "Abcdef1!", "CNA"
        );

        when(teacherRepository.existsTeacherByEmail(any())).thenReturn(false);
        when(schoolRepository.findSchoolByName("CNA")).thenReturn(Optional.empty());
        when(schoolRepository.save(any())).thenReturn(school);
        when(teacherRepository.save(any())).thenReturn(teacher);
        when(tokenService.generateToken(any())).thenReturn("token");
        when(passwordEncoder.encode(any())).thenReturn("hash");

        TokenResponse result = authService.register(request);

        assertNotNull(result);
        verify(schoolRepository, times(1)).save(any());
    }

    @Test
    void registerShouldReuseExistingSchool() {
        TeacherRequest request = new TeacherRequest(
                "Thulio", "thulio@cna.com", "Abcdef1!", "Abcdef1!", "CNA"
        );

        when(teacherRepository.existsTeacherByEmail(any())).thenReturn(false);
        when(schoolRepository.findSchoolByName("CNA")).thenReturn(Optional.of(school));
        when(teacherRepository.save(any())).thenReturn(teacher);
        when(tokenService.generateToken(any())).thenReturn("token");
        when(passwordEncoder.encode(any())).thenReturn("hash");

        authService.register(request);

        verify(schoolRepository, never()).save(any());
    }

    @Test
    void loginShouldThrowWhenEmailNotFound() {
        LoginRequest request = new LoginRequest("notfound@cna.com", "Abcdef1!");

        when(teacherRepository.findByEmail("notfound@cna.com")).thenReturn(Optional.empty());

        assertThrows(Exception.class, () ->
                authService.login(request)
        );
    }

    @Test
    void loginShouldThrowWhenPasswordIsWrong() {
        LoginRequest request = new LoginRequest("thulio@cna.com", "SenhaErrada1!");

        when(teacherRepository.findByEmail("thulio@cna.com")).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches("SenhaErrada1!", "hash")).thenReturn(false);

        assertThrows(IllegalStateException.class, () ->
                authService.login(request)
        );
    }

    @Test
    void loginShouldReturnTokenWhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("thulio@cna.com", "Abcdef1!");

        when(teacherRepository.findByEmail("thulio@cna.com")).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches("Abcdef1!", "hash")).thenReturn(true);
        when(tokenService.generateToken(teacher)).thenReturn("jwt_token");

        TokenResponse result = authService.login(request);

        assertNotNull(result);
        assertEquals("jwt_token", result.token());
    }
}