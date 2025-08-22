package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.Cause;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CauseRepository  extends JpaRepository<Cause,Long> {
}
