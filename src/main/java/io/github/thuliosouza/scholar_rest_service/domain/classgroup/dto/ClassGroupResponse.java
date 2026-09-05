package io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto;

import io.github.thuliosouza.scholar_rest_service.domain.classgroup.ClassGroup;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.Module;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.Teacher;

import java.util.Optional;
import java.util.UUID;

public record ClassGroupResponse(
        UUID id,
        int number,
        Module module,
        String teacherName
) {

    public static ClassGroupResponse from(ClassGroup classGroup) {
        return new ClassGroupResponse(
                classGroup.getId(),
                classGroup.getNumber(),
                classGroup.getModule(),
                Optional.ofNullable(classGroup.getTeacher())
                        .map(Teacher::getName)
                        .orElse(null)
        );
    }
}