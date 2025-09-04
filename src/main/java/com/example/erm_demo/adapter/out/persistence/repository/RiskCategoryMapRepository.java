package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.CauseMapEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskCategoryMapRepository extends JpaRepository<RiskCategoryMapEntity,Long> {

    List<RiskCategoryMapEntity> findByRiskCategoryId(Long id);
    void deleteByRiskCategoryId(Long id);
}

