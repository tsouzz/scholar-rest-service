package io.github.thuliosouza.scholar_rest_service.domain.classgroup;

import io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto.ClassGroupRequest;
import io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto.ClassGroupResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/class-groups")
@RequiredArgsConstructor
public class ClassGroupController {

    private final ClassGroupService classGroupService;

    @PostMapping
    public ResponseEntity<ClassGroupResponse> create(@RequestBody @Valid ClassGroupRequest request) {
        ClassGroupResponse response = classGroupService.createClassGroup(request);
        URI location = URI.create("/class-groups/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ClassGroupResponse>> findAll() {
        return ResponseEntity.ok(classGroupService.findAllByAuthenticatedTeacher());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassGroupResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(classGroupService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassGroupResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid ClassGroupRequest request
    ) {
        return ResponseEntity.ok(classGroupService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        classGroupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}