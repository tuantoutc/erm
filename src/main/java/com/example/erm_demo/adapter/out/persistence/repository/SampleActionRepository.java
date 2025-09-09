package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.SampleActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SampleActionRepository extends JpaRepository<SampleActionEntity,Long>, JpaSpecificationExecutor<SampleActionEntity> {

}
