package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeAttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskTypeAttributeValueRepository extends JpaRepository<RiskTypeAttributeValueEntity,Long> {

    List<RiskTypeAttributeValueEntity> findByRiskTypesAttributeId(Long id);
    void deleteByRiskTypesAttributeId(Long id);


}
