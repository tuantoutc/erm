package com.example.erm_demo.adapter.out.persistence.entity;

import com.example.erm_demo.domain.enums.ActionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name ="sample_action_map")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SampleActionMapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    ActionType type;

    @Column(name = "preventive_measure_id", nullable = false)
    Long preventiveMeasureId;
    @Column(name = "sample_action_id", nullable = false)
    Long sampleActionId;

    @Column(name ="department_id", nullable = false)
    Long departmentId;

    @Lob
    @Column(columnDefinition = "TEXT")
    String content;
}
