package com.example.erm_demo.adapter.out.persistence.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "risk_types_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RiskTypeAttributeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @Column(name="risk_type_id", nullable = false)
    Long riskTypeId;

    @Column(name="attribute_group_id", nullable = false)
    Long attributeGroupId;

    @Column(name="attribute_id", nullable = false)
    Long attributeId;


}
