package com.example.erm_demo.adapter.out.persistence.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "cause_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CauseCategory {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;
    String code;
    String name;
    @Lob /// (Large Object) báo cho JPA biết đây là trường dữ liệu lớn.
    @Column(columnDefinition = "TEXT")
    String description;
    @Lob
    @Column(columnDefinition = "TEXT")
    String note;

    @OneToMany(
        mappedBy = "causeCategory",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    List<Cause> causes;

    @OneToMany(mappedBy = "causeCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<CauseCategoryMap> causeCategoryMaps;


}
