package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.AttributeGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AttributeGroupRepository extends JpaRepository<AttributeGroupEntity, Long> , JpaSpecificationExecutor<AttributeGroupEntity> {

    List<AttributeGroupEntity> findByIdIn(List<Long> ids);


}
