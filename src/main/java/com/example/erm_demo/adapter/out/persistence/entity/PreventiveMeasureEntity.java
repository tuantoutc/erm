package com.example.erm_demo.adapter.out.persistence.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name ="preventive_measures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PreventiveMeasureEntity {

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

}
