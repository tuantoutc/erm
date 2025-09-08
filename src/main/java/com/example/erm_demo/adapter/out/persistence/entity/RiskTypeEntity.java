package com.example.erm_demo.adapter.out.persistence.entity;

import com.example.erm_demo.domain.enums.ObjectType;
import com.example.erm_demo.domain.enums.Origin;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "risk_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RiskTypeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String code;

    @Column(unique = true)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin")
    Origin origin; // Bên ngoài, nội bộ

    @Lob
    @Column(columnDefinition = "TEXT")
    String note;

    @Column(name = "is_active")
    Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "object")
    ObjectType object; // con người, quy trình, hệ thống, bên thứ 3


}
