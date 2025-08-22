package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.CauseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CauseCategoryRepository extends JpaRepository<CauseCategory,Long> {
}
