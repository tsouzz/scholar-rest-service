package io.github.thuliosouza.scholar_rest_service.domain.student;

import io.github.thuliosouza.scholar_rest_service.domain.student.dto.StudentRequest;
import io.github.thuliosouza.scholar_rest_service.domain.student.dto.StudentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> create(@RequestBody @Valid StudentRequest request) {
        StudentResponse response = studentService.createStudent(request);
        URI location = URI.create("/students/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @GetMapping("/class-group/{classGroupId}")
    public ResponseEntity<List<StudentResponse>> getClassGroup(@PathVariable UUID classGroupId) {
        return ResponseEntity.ok(studentService.findByClassGroupId(classGroupId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateName(
            @PathVariable UUID id,
            @RequestBody @Valid StudentRequest request
    ) {
        return ResponseEntity.ok(studentService.updateName(id, request.name()));
    }

    @PostMapping("/{id}/transfer")
    public ResponseEntity<StudentResponse> transfer(
            @PathVariable UUID id,
            @RequestParam UUID classGroupId
    ) {
        return ResponseEntity.ok(studentService.transferStudent(id, classGroupId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
