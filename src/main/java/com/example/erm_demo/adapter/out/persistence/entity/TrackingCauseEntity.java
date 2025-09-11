package com.example.erm_demo.adapter.out.persistence.entity;

import com.example.erm_demo.domain.enums.ObjectApplicableType;
import com.example.erm_demo.domain.enums.State;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tracking_causes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TrackingCauseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "cause_category_id", nullable = false)
    Long causeCategoryId;

    @Column(name = "cause_id", nullable = false)
    Long causeId;

    @Column(name = "sample_action_id", nullable = false)
    Long sampleActionId;

    @Column(name="count")
    Long count;

    @Enumerated(EnumType.STRING)
    @Column(name="object_applicable_type")
    ObjectApplicableType objectApplicableType;

    @Enumerated(EnumType.STRING)
    @Column(name="state")
    State state;

}
