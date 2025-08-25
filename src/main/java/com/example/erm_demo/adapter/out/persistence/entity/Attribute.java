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
@Table(name = "attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attribute {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String code;
    @Column(unique = true)
    String name;
    @Column(name ="display_type")
    String displayType;
    @Column(name ="data_type")
    String dataType;
    @Column(name ="source_type")
    SourceType sourceType;

    @Lob
    @Column(columnDefinition = "TEXT")
    String description;
    @Column(name = "is_active")
    Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_group_id", nullable = false)
    AttributeGroup attributeGroup;


    @OneToMany(mappedBy = "attribute", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<AttributeValue> attributeValues;




}
