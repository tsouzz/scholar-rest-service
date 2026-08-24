package io.github.thuliosouza.scholar_rest_service.domain.teacher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    public Optional<Teacher> findByEmail(String email);

    public boolean existsTeacherByEmail(String email);
}
