package com.example.erm_demo.adapter.out.persistence.entity;


import com.example.erm_demo.domain.enums.SourceType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "attribute_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeGroupEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String code;
    @Column(unique = true)
    String name;

    @Column(name ="source_type")
    SourceType sourceType;

    @Lob
    @Column(columnDefinition = "TEXT")
    String description;
    @Column(name = "is_active")
    Boolean isActive;

//    @OneToMany(
//            mappedBy = "attributeGroup",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    List<AttributeEntity> attributeEntities;
}
