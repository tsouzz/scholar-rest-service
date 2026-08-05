package io.github.thuliosouza.scholar_rest_service.domain.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    public List<Student> findByClassGroupId(UUID classGroupId);

}
