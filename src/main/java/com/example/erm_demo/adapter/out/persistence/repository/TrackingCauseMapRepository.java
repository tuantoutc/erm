package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.TrackingCauseMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TrackingCauseMapRepository extends JpaRepository<TrackingCauseMapEntity,Long>  {

    List<TrackingCauseMapEntity> findByTrackingCauseId(Long id);

}
