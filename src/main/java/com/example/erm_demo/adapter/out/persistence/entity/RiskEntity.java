package com.example.erm_demo.adapter.out.persistence.entity;

import com.example.erm_demo.domain.enums.PriorityLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "risks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RiskEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String code;

    @Column(unique = true)
    String name;

    @Column(name ="system_id")
    Long systemId;

    @Column(name ="risk_type_id")
    Long riskTypeId;

    @Column(name ="risk_category_id")
    Long riskCategoryId;

    @Column(name ="reporter_id")
    Long reporterId;

    @Column (name = "recorded_time")
    Date recordedTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority_level")
    PriorityLevel priorityLevel;

    @Lob
    @Column(columnDefinition = "TEXT", name="description")
    String description;

    @Lob
    @Column(columnDefinition = "TEXT", name="expected_consequence")
    String expectedConsequence;

    @Column(name ="level")
    Long level;

    @Column(name ="point")
    Long point;

}
