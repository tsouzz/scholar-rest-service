package io.github.thuliosouza.scholar_rest_service.domain.school;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SchoolRepository extends JpaRepository<School, UUID> {

    public School findSchoolByName(String name);

}
