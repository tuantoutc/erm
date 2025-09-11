package com.example.erm_demo.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "risk_cause_line")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RiskCauseLineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "risk_id", nullable = false)
    Long riskId;

    @Column(name = "tracking_cause_id", nullable = false)
    Long trackingCauseId;

    @Column(name = "sample_action_id", nullable = false)
    Long sampleActionId;
}
