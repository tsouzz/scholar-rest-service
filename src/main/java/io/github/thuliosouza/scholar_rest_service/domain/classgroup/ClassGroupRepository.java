package io.github.thuliosouza.scholar_rest_service.domain.ClassGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup, UUID> {

    public List<ClassGroup> findAllByTeacherId(UUID teacherId);

    public int countByModuleAndTeacherId(Module module, UUID teacherId);
}
