package io.github.thuliosouza.scholar_rest_service.domain.classgroup;

import io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto.ClassGroupRequest;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto.ClassGroupResponse;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.exception.ClassGroupNotFoundException;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassGroupService {

    private final ClassGroupRepository classGroupRepository;

    @Transactional
    public ClassGroupResponse createClassGroup(ClassGroupRequest request) {
        Teacher teacher = getAuthenticatedTeacher();

        int number = classGroupRepository
                .countByModuleAndTeacherId(request.module(), teacher.getId()) + 1;

        ClassGroup classGroup = ClassGroup.builder()
                .number(number)
                .module(request.module())
                .teacher(teacher)
                .build();

        return ClassGroupResponse.from(classGroupRepository.save(classGroup));
    }

    public List<ClassGroupResponse> findAllByAuthenticatedTeacher() {
        Teacher teacher = getAuthenticatedTeacher();
        return classGroupRepository.findAllByTeacherId(teacher.getId())
                .stream()
                .map(ClassGroupResponse::from)
                .toList();
    }

    public ClassGroupResponse findById(UUID classGroupId) {
        return ClassGroupResponse.from(getClassGroupEntity(classGroupId));
    }

    @Transactional
    public ClassGroupResponse update(UUID classGroupId, ClassGroupRequest request) {
        ClassGroup classGroup = getClassGroupEntity(classGroupId);
        classGroup.setModule(request.module());
        return ClassGroupResponse.from(classGroupRepository.save(classGroup));
    }

    @Transactional
    public void delete(UUID classGroupId) {
        classGroupRepository.delete(getClassGroupEntity(classGroupId));
    }

    private ClassGroup getClassGroupEntity(UUID classGroupId) {
        return classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new ClassGroupNotFoundException(
                        String.format("Turma com id %s não encontrada.", classGroupId)
                ));
    }

    private Teacher getAuthenticatedTeacher() {
        return (Teacher) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}