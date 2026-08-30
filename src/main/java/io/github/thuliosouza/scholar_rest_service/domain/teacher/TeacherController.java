package io.github.thuliosouza.scholar_rest_service.domain.teacher;

import io.github.thuliosouza.scholar_rest_service.domain.teacher.dto.TeacherRequest;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.dto.TeacherResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponse> getTeacher(@PathVariable @Valid UUID id){
        TeacherResponse teacher = teacherService.findById(id);
        return ResponseEntity.ok(teacher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponse> updateTeacher(
            @PathVariable UUID id,
            @RequestBody @Valid TeacherRequest teacherRequest
    ) {
        TeacherResponse updatedTeacher = teacherService.update(id, teacherRequest);
        return ResponseEntity.ok(updatedTeacher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}