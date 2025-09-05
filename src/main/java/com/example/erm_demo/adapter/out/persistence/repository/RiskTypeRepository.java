package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RiskTypeRepository extends JpaRepository<RiskTypeEntity,Long>, JpaSpecificationExecutor<RiskTypeEntity> {

}
