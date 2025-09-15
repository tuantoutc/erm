package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.RiskCauseLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskCauseLineRepository extends JpaRepository<RiskCauseLineEntity,Long> {

    List<RiskCauseLineEntity> findByRiskId(Long id);

}
