package com.example.erm_demo.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "risk_attribute_line_values")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RiskAttributeLineValueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "risk_lines_id", nullable = false)
    Long riskLinesId;

    @Column(name = "attribute_values_id", nullable = false)
    Long attributeValuesId;

    @Lob
    @Column(columnDefinition = "TEXT", name="text_value")
    String textValue;

}
