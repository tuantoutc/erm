package com.example.erm_demo.adapter.out.persistence.entity;

import com.example.erm_demo.domain.enums.ActionType;
import com.example.erm_demo.domain.enums.State;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "tracking_actions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TrackingActionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "preventive_measure_id", nullable = false)
    Long preventiveMeasureId;

    @Enumerated(EnumType.STRING)
    @Column(name= "type")
    ActionType actionType;

    @Column(name = "department_id", nullable = false)
    Long departmentId;

    @Column(name="content" )
    String content;

    @Column(name="plan_date")
    Date planDate;

}
