package io.github.thuliosouza.scholar_rest_service.domain.student;

import io.github.thuliosouza.scholar_rest_service.domain.classgroup.ClassGroup;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.ClassGroupRepository;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.exception.ClassGroupNotFoundException;
import io.github.thuliosouza.scholar_rest_service.domain.student.exception.InvalidOperationException;
import io.github.thuliosouza.scholar_rest_service.domain.student.exception.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassGroupRepository classGroupRepository;

    @Transactional
    public Student createStudent(String name, ClassGroup classGroup) {
        Student student = Student.builder()
                .name(name)
                .classGroup(classGroup)
                .build();

        return studentRepository.save(student);
    }

    @Transactional
    public Student transferStudent(
            UUID studentId,
            UUID classGroupId
    ) throws InvalidOperationException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado!"));

        ClassGroup classGroup = classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new ClassGroupNotFoundException("Turma não encontrada!"));

        if (student.getClassGroup().getModule() != classGroup.getModule()) {
            throw new InvalidOperationException("Transferência inválida: a turma destino pertence a um módulo diferente.");
        }

        student.setClassGroup(classGroup);
        return student;
    }

    @Transactional
    public Student updateName(UUID studentId, String name) {
        Student student = findById(studentId);
        student.setName(name);

        return student;
    }

    @Transactional
    public void delete(UUID studentId) {
        studentRepository.deleteById(studentId);
    }

    public Student findById(UUID studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado!"));
    }
}
