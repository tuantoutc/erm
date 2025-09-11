package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.RiskTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskTagRepository extends JpaRepository<RiskTagEntity,Long>  {

    List<RiskTagEntity> findByRiskId(Long riskId);
}
