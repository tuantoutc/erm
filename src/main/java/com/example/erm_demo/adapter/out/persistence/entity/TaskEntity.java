//package com.example.erm_demo.adapter.out.persistence.entity;
//
//import com.example.erm_demo.domain.enums.PriorityLevel;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.experimental.FieldDefaults;
//
//import java.util.Date;
//
//@Entity
//@Table(name = "tasks")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class TaskEntity {
//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    Long id;
//
//    @Column(unique = true)
//    String code;
//
//    @Column(unique = true)
//    String name;
//
//    @Column(name ="parent_id")
//    Long parentId;
//
//    @Column(name ="assigner_id")
//    Long assignerId;
//
//    @Column(name ="employee_id")
//    Long employeeId;
//
//
//    @Column (name = "start_date")
//    Date startDate;
//
//    @Column (name = "end_date")
//    Date endDate;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "priority_level")
//    PriorityLevel priorityLevel;
//
//    @Lob
//    @Column(columnDefinition = "TEXT", name="content")
//    String content;
//
//    @Lob
//    @Column(columnDefinition = "TEXT", name="expected_consequence")
//    String expectedConsequence;
//
//    @Column(name ="level")
//    Long level;
//
//    @Column(name ="point")
//    Long point;
//
//}
