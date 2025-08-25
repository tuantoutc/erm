package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.AttributeGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttributeGroupRepository extends JpaRepository<AttributeGroup, Long> {


    @Query("SELECT ag FROM AttributeGroup ag " +
            "WHERE (:keyword IS NULL OR LOWER(ag.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(ag.code) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:isActive IS NULL OR ag.isActive = :isActive)")
    Page<AttributeGroup> searchBy(@Param("keyword") String keyword,
                                @Param("isActive") Boolean isActive,
                                Pageable pageable);
}
