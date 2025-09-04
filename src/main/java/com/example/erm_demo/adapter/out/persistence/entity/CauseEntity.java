package com.example.erm_demo.adapter.out.persistence.entity;


import com.example.erm_demo.domain.enums.Origin;
import com.example.erm_demo.domain.enums.TypeERM;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "causes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CauseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String code;
    @Column(unique = true)
    String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    TypeERM type; //  Sự cố, rủi ro

    @Enumerated(EnumType.STRING)
    @Column(name = "origin")
    Origin origin; // Bên ngoài, nội bộ
    @Lob
    @Column(columnDefinition = "TEXT")
    String note;
    @Column(name = "is_active")
    Boolean isActive;

    @Column(name = "cause_category_id")
    Long causeCategoryId;


}
