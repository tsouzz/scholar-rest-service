package io.github.thuliosouza.scholar_rest_service.domain.activity;

import io.github.thuliosouza.scholar_rest_service.domain.activity.dto.ActivityRequest;
import io.github.thuliosouza.scholar_rest_service.domain.activity.dto.ActivityResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> create(@RequestBody @Valid ActivityRequest request) {
        ActivityResponse response = activityService.createActivity(request);
        URI location = URI.create("/activities/"+ response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<ActivityResponse>> getActivity(@PathVariable UUID studentId) {
        return ResponseEntity.ok(activityService.findByStudent(studentId));
    }


}
