package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.TrackingCauseMapEntity;
import com.example.erm_demo.adapter.out.persistence.entity.TrackingCauseMapFailProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TrackingCauseMapFailProductRepository extends JpaRepository<TrackingCauseMapFailProductEntity,Long>  {

}
