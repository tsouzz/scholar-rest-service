package io.github.thuliosouza.scholar_rest_service.domain.teacher;

import io.github.thuliosouza.scholar_rest_service.domain.school.School;
import io.github.thuliosouza.scholar_rest_service.domain.school.SchoolRepository;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.exception.TeacherNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Teacher createTeacher(
            String name,
            String email,
            String rawPassword,
            String schoolName
    ) {
        School school = schoolRepository.findSchoolByName(schoolName)
                .orElseGet(() -> schoolRepository.save(
                        School.builder()
                                .name(schoolName)
                                .build()
                ));

        String passwordHash = passwordEncoder.encode(rawPassword);

        Teacher teacher = Teacher.builder()
                .name(name)
                .email(email)
                .passwordHash(passwordHash)
                .school(school)
                .build();

        return teacherRepository.save(teacher);
    }

    @Transactional
    public Teacher updateTeacher(
            UUID id,
            String name,
            String email,
            String rawPassword
    ){
        Teacher teacher = findById(id);

        teacher.setName(name);
        teacher.setEmail(email);

        if (rawPassword != null && !rawPassword.isBlank()) {
            teacher.setPasswordHash(passwordEncoder.encode(rawPassword));
        }

        return teacher;
    }

    @Transactional
    public void deleteTeacher(UUID teacherId){
        teacherRepository.deleteById(teacherId);
    }

    public Teacher findById(UUID teacherId){
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException(String.format("Professor com id %s não encontrado", teacherId)));
    }

    public Teacher findByEmail(String email){
        return teacherRepository.findByEmail(email)
                .orElseThrow();
    }
}
