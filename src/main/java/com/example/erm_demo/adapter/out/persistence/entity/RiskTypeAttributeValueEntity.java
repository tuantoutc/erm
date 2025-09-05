package com.example.erm_demo.adapter.out.persistence.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "risk_types_attributes_values")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RiskTypeAttributeValueEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @Column(name="risk_types_attribute_id", nullable = false)
    Long riskTypesAttributeId;

    @Column(name="attribute_value_id", nullable = false)
    Long attributeValueId;

    @Column(name="text_value")
    String textValue;


}
