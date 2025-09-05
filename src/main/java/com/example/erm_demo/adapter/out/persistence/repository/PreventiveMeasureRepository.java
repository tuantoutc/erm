package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.PreventiveMeasureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PreventiveMeasureRepository extends JpaRepository<PreventiveMeasureEntity,Long>, JpaSpecificationExecutor<PreventiveMeasureEntity> {


}
