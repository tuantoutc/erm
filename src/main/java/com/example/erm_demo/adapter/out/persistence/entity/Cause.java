package com.example.erm_demo.adapter.out.persistence.entity;


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
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;
    String code;
    String name;
    String type; //  Sự cố, rủi ro
    String origin; // Bên ngoài, nội bộ
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

    @OneToMany(mappedBy = "cause", fetch = FetchType.LAZY)
    List<CauseMap> causeMaps;
}
