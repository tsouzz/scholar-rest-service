package io.github.thuliosouza.scholar_rest_service.domain.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    public List<Activity> findAllByStudentId(UUID studentId);

    public Activity findByStudentIdAndActivityType(UUID studentId, ActivityType activityType);


}
