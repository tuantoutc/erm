package com.example.erm_demo.adapter.out.persistence.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name ="sample_actions")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SampleActionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String code;

    @Column(unique = true)
    String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    String note;

    @Column(name = "is_active")
    Boolean isActive;

    @Column(name = "risk_type_id", nullable = false)
    Long riskTypeId;

    @Column(name="cause_category_id", nullable = false)
    Long causeCategoryId;












}
