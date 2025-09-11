package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.RiskEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RiskRepository extends JpaRepository<RiskEntity,Long>, JpaSpecificationExecutor<RiskEntity> {

}
