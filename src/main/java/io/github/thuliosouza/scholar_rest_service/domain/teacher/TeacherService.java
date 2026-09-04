package io.github.thuliosouza.scholar_rest_service.domain.teacher;

import io.github.thuliosouza.scholar_rest_service.domain.school.School;
import io.github.thuliosouza.scholar_rest_service.domain.school.SchoolRepository;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.dto.TeacherRequest;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.dto.TeacherResponse;
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

    public TeacherResponse findById(UUID teacherId) {
        return TeacherResponse.from(getTeacherEntity(teacherId));
    }

    @Transactional
    public TeacherResponse update(UUID teacherId, TeacherRequest request) {
        Teacher teacher = getTeacherEntity(teacherId);

        teacher.setName(request.name());
        teacher.setEmail(request.email());

        if (request.password() != null && !request.password().isBlank()) {
            teacher.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        if (request.schoolName() != null && !request.schoolName().isBlank()) {
            School school = schoolRepository.findSchoolByName(request.schoolName())
                    .orElseGet(() -> schoolRepository.save(
                            School.builder()
                                    .name(request.schoolName())
                                    .build()
                    ));
            teacher.setSchool(school);
        }

        return TeacherResponse.from(teacherRepository.save(teacher));
    }

    @Transactional
    public void delete(UUID teacherId) {
        teacherRepository.delete(getTeacherEntity(teacherId));
    }

    private Teacher getTeacherEntity(UUID teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException(
                        String.format("Professor com id %s não encontrado.", teacherId)
                ));
    }
}