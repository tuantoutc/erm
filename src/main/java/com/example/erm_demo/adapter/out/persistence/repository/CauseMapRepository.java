package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.CauseMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CauseMapRepository  extends JpaRepository<CauseMapEntity,Long> {

    List<CauseMapEntity> findByCauseId(Long id);
    void deleteByCauseId(Long id);
}
