package com.example.erm_demo.adapter.out.persistence.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "risk_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RiskCategory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String code;
    @Column(unique = true)
    String name;
    @Lob
    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "is_active")
    Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    RiskCategory parent;

    @OneToMany(mappedBy = "riskCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<RiskCategoryMap> riskCategoryMaps;



}
