package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.RiskCauseLineActionLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskCauseLineActionLineRepository extends JpaRepository<RiskCauseLineActionLineEntity,Long> {
    List<RiskCauseLineActionLineEntity> findByRiskCauseLineId(Long id);

}
