package io.github.thuliosouza.scholar_rest_service.domain.classgroup;

import io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto.ClassGroupRequest;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto.ClassGroupResponse;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.exception.ClassGroupNotFoundException;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassGroupServiceTest {

    @Mock
    private ClassGroupRepository classGroupRepository;

    private ClassGroupService classGroupService;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        classGroupService = new ClassGroupService(classGroupRepository);

        teacher = Teacher.builder()
                .id(UUID.randomUUID())
                .name("Thulio Souza")
                .email("thulio@cna.com")
                .passwordHash("hash")
                .build();

        var authentication = new UsernamePasswordAuthenticationToken(
                teacher, null, teacher.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void createClassGroupShouldAssignNumberOneWhenFirstOfModule() {
        ClassGroupRequest request = new ClassGroupRequest(Module.INTERMEDIATE_1);

        when(classGroupRepository.countByModuleAndTeacherId(Module.INTERMEDIATE_1, teacher.getId()))
                .thenReturn(0);

        ClassGroup saved = ClassGroup.builder()
                .id(UUID.randomUUID())
                .number(1)
                .module(Module.INTERMEDIATE_1)
                .teacher(teacher)
                .build();

        when(classGroupRepository.save(any())).thenReturn(saved);

        ClassGroupResponse result = classGroupService.createClassGroup(request);

        assertEquals(1, result.number());
        assertEquals(Module.INTERMEDIATE_1, result.module());
        verify(classGroupRepository, times(1)).save(any());
    }

    @Test
    void createClassGroupShouldAssignNumberTwoWhenSecondOfModule() {
        ClassGroupRequest request = new ClassGroupRequest(Module.INTERMEDIATE_1);

        when(classGroupRepository.countByModuleAndTeacherId(Module.INTERMEDIATE_1, teacher.getId()))
                .thenReturn(1);

        ClassGroup saved = ClassGroup.builder()
                .id(UUID.randomUUID())
                .number(2)
                .module(Module.INTERMEDIATE_1)
                .teacher(teacher)
                .build();

        when(classGroupRepository.save(any())).thenReturn(saved);

        ClassGroupResponse result = classGroupService.createClassGroup(request);

        assertEquals(2, result.number());
        verify(classGroupRepository, times(1)).save(any());
    }

    @Test
    void createClassGroupShouldHaveIndependentNumbersForDifferentModules() {
        when(classGroupRepository.countByModuleAndTeacherId(Module.BASIC_1, teacher.getId()))
                .thenReturn(2);
        when(classGroupRepository.countByModuleAndTeacherId(Module.ADVANCED_1, teacher.getId()))
                .thenReturn(0);

        ClassGroup savedBasic = ClassGroup.builder()
                .id(UUID.randomUUID()).number(3).module(Module.BASIC_1).teacher(teacher).build();
        ClassGroup savedAdvanced = ClassGroup.builder()
                .id(UUID.randomUUID()).number(1).module(Module.ADVANCED_1).teacher(teacher).build();

        when(classGroupRepository.save(any()))
                .thenReturn(savedBasic)
                .thenReturn(savedAdvanced);

        ClassGroupResponse basic = classGroupService.createClassGroup(new ClassGroupRequest(Module.BASIC_1));
        ClassGroupResponse advanced = classGroupService.createClassGroup(new ClassGroupRequest(Module.ADVANCED_1));

        assertEquals(3, basic.number());
        assertEquals(1, advanced.number());
    }

    @Test
    void findByIdShouldThrowWhenClassGroupNotFound() {
        UUID id = UUID.randomUUID();

        when(classGroupRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ClassGroupNotFoundException.class, () ->
                classGroupService.findById(id)
        );
    }

    @Test
    void deleteShouldThrowWhenClassGroupNotFound() {
        UUID id = UUID.randomUUID();

        when(classGroupRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ClassGroupNotFoundException.class, () ->
                classGroupService.delete(id)
        );

        verify(classGroupRepository, never()).delete(any());
    }

    @Test
    void findAllShouldReturnClassGroupsOfAuthenticatedTeacher() {
        ClassGroup classGroup = ClassGroup.builder()
                .id(UUID.randomUUID())
                .number(1)
                .module(Module.BASIC_1)
                .teacher(teacher)
                .build();

        when(classGroupRepository.findAllByTeacherId(teacher.getId()))
                .thenReturn(List.of(classGroup));

        List<ClassGroupResponse> result = classGroupService.findAllByAuthenticatedTeacher();

        assertEquals(1, result.size());
        assertEquals(Module.BASIC_1, result.get(0).module());
    }
}