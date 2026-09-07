package io.github.thuliosouza.scholar_rest_service.domain.classgroup;

import io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto.ClassGroupRequest;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto.ClassGroupResponse;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.exception.ClassGroupNotFoundException;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.Teacher;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.TeacherRepository;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.exception.TeacherNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassGroupService {

    private final ClassGroupRepository classGroupRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public ClassGroupResponse createClassGroup(ClassGroupRequest request) {
        Teacher teacher = teacherRepository.findById(request.teacherId())
                .orElseThrow(() -> new TeacherNotFoundException("Professor não encontrado!"));

        ClassGroup classGroup = ClassGroup.builder()
                .number(request.number())
                .module(request.module())
                .teacher(teacher)
                .build();

        classGroupRepository.save(classGroup);
        return ClassGroupResponse.from(classGroup);
    }

    public List<ClassGroupResponse> findClassGroupsByTeacher(UUID teacherId){
        return classGroupRepository.findAllByTeacherId(teacherId).stream()
                .map(ClassGroupResponse::from)
                .toList();
    }

    @Transactional
    public ClassGroupResponse update(UUID classGroupId, Module module){
        ClassGroup classGroup = classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new ClassGroupNotFoundException("Classe não encontrada!"));
        classGroup.setModule(module);

        return ClassGroupResponse.from(classGroup);
    }

    @Transactional
    public void deleteClassGroup(UUID classGroupId){
        if(!classGroupRepository.findById(classGroupId).isPresent()){
            throw new ClassGroupNotFoundException("Classe não encontrada!");
        }
        classGroupRepository.deleteById(classGroupId);
    }
}
