package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.AttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeValueRepository extends JpaRepository<AttributeValueEntity, Long> {

    void deleteByAttributeId(Long id);
    List<AttributeValueEntity> findByAttributeId(Long attributeId);
    List<AttributeValueEntity> findByAttributeIdIn(List<Long> attributeIds);
}
