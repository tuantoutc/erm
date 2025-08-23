package com.example.erm_demo.adapter.out.persistence.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "cause_categories_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CauseCategoryMap {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cause_categories_id", nullable = false)
    CauseCategory causeCategory;
    @Column(name = "system_id", nullable = false)
    Long systemId;

}
