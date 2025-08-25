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
public class Cause {
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

    @Lob /// (Large Object) báo cho JPA biết đây là trường dữ liệu lớn.
    @Column(columnDefinition = "TEXT")
    String description;
    @Lob
    @Column(columnDefinition = "TEXT")
    String note;
    @Column(name = "is_active")
    Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cause_category_id", nullable = false)
    CauseCategory causeCategory;

    @OneToMany(mappedBy = "cause", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<CauseMap> causeMaps;
}
