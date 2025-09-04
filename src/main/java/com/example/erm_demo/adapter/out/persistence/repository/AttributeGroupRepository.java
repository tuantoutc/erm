package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.AttributeGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AttributeGroupRepository extends JpaRepository<AttributeGroupEntity, Long> , JpaSpecificationExecutor<AttributeGroupEntity> {

}
