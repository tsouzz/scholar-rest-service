package io.github.thuliosouza.scholar_rest_service.domain.student;

import io.github.thuliosouza.scholar_rest_service.domain.ClassGroup.ClassGroup;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "class_group_id",  nullable = false)
    private ClassGroup classGroup;

}
