package io.github.thuliosouza.scholar_rest_service.domain.classgroup;

import io.github.thuliosouza.scholar_rest_service.domain.classgroup.exception.ClassGroupNotFoundException;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassGroupService {

    private final ClassGroupRepository classGroupRepository;

    @Transactional
    public ClassGroup createClassGroup(
            int number,
            Module module,
            Teacher teacher
    ){
        ClassGroup classGroup = ClassGroup.builder()
                .number(number)
                .module(module)
                .teacher(teacher)
                .build();

        return classGroupRepository.save(classGroup);
    }

    public List<ClassGroup> findClassGroupsByTeacher(UUID teacherId){
        return classGroupRepository.findAllByTeacherId(teacherId);
    }

    @Transactional
    public ClassGroup update(UUID classGroupId, Module module){
        ClassGroup classGroup = classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new ClassGroupNotFoundException("Classe não encontrada!"));
        classGroup.setModule(module);

        return classGroup;
    }

    @Transactional
    public void deleteClassGroup(UUID classGroupId){
        classGroupRepository.deleteById(classGroupId);
    }
}
