package io.github.thuliosouza.scholar_rest_service.domain.teacher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    public Teacher findByEmail(String email);



}
