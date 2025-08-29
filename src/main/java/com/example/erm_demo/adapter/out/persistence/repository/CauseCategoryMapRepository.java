package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CauseCategoryMapRepository extends JpaRepository<CauseCategoryMapEntity,Long> {

    List<CauseCategoryMapEntity> findByCauseCategoryId(Long id);
    void deleteByCauseCategoryId(Long id);
}
