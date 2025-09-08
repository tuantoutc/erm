package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeAttributeEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskTypeAttributeRepository extends JpaRepository<RiskTypeAttributeEntity,Long> {

    List<RiskTypeAttributeEntity> findByRiskTypeId(Long id);
    void deleteByRiskTypeId(Long id);

}
