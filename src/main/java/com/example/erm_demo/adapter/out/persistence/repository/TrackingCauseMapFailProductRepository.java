package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.TrackingCauseMapFailProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TrackingCauseMapFailProductRepository extends JpaRepository<TrackingCauseMapFailProductEntity,Long>  {

    List<TrackingCauseMapFailProductEntity> findByTrackingCausesMapId(Long trackingCausesMapId);

}
