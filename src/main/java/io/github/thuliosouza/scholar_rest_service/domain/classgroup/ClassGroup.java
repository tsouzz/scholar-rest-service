package io.github.thuliosouza.scholar_rest_service.domain.classgroup;

import io.github.thuliosouza.scholar_rest_service.domain.teacher.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "class_groups")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Module module;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    public String getName() {
        return module.getAcronym() + "-" + String.format("%04d", number);
    }

}