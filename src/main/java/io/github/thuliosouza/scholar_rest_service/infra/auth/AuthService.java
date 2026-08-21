package io.github.thuliosouza.scholar_rest_service.infra.auth;

import io.github.thuliosouza.scholar_rest_service.domain.school.School;
import io.github.thuliosouza.scholar_rest_service.domain.school.SchoolRepository;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.Teacher;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.TeacherRepository;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.dto.TeacherRequest;
import io.github.thuliosouza.scholar_rest_service.infra.auth.dto.LoginRequest;
import io.github.thuliosouza.scholar_rest_service.infra.auth.dto.TokenResponse;
import io.github.thuliosouza.scholar_rest_service.infra.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse register(TeacherRequest request) {
        validadePasswordStrenght(request.password());
        validadePasswordMatch(request.password(), request.confirmPassword());

        if (teacherRepository.existsTeacherByEmail(request.email())){
            throw new IllegalStateException("Email já cadastrado");
        }

        School school = schoolRepository.findSchoolByName(request.name())
                .orElseGet(() -> schoolRepository.save(
                        School.builder()
                                .name(request.name())
                                .build()
                ));

        Teacher teacher = Teacher.builder()
                .name(request.name())
                .email(request.email())
                .school(school)
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();

        teacherRepository.save(teacher);
        return new TokenResponse(tokenService.generateToken(teacher));
    }

    public TokenResponse login(LoginRequest request) {
        Teacher teacher = teacherRepository.findByEmail(request.email())
                .orElseThrow();

        if (!passwordEncoder.matches(request.password(), teacher.getPasswordHash())) {
            throw new IllegalStateException("Credenciais inválidas");
        }

        return new TokenResponse(tokenService.generateToken(teacher));
    }

    private void validadePasswordStrenght(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Senha deve conter no minimo 8 caracteres.");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Senha deve conter no minimo uma letra maiúscula.");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Senha deve conter no mínimo um número.");
        }
    }

    private void validadePasswordMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Senhas não conferem.");
        }
    }
}