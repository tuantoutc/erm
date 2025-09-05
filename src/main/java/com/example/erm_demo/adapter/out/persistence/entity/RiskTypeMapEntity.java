package com.example.erm_demo.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "risk_type_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RiskTypeMapEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    @Column(name = "system_id", nullable = false)
    Long systemId;
    @Column(name="risk_type_id", nullable = false)
    Long riskTypeId;

}
