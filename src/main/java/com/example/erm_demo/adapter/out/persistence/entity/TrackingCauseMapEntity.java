package com.example.erm_demo.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tracking_cause_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TrackingCauseMapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "tracking_cause_id", nullable = false)
    Long trackingCauseId;

    @Column(name = "department_id", nullable = false)
    Long departmentId;

    @Column(name = "position_id", nullable = false)
    Long positionId;

    @Column(name = "employee_id", nullable = false)
    Long employeeId;

    @Column(name = "product_id", nullable = false)
    Long productId;

    @Column(name = "parner_type", nullable = false)
    Long parnerType;

    @Column(name = "group_parner_id", nullable = false)
    Long groupParnerId;

    @Column(name = "parner_id", nullable = false)
    Long parnerId;



}
