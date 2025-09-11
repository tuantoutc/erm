package com.example.erm_demo.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "risk_attribute_line")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RiskAttributeLineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "attribute_group_id", nullable = false)
    Long atributeGroupId;

    @Column(name = "attribute_id", nullable = false)
    Long attributeId;

    @Column(name = "risk_id", nullable = false)
    Long riskId;
}
