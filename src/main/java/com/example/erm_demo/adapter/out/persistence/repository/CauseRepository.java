package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.Cause;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategory;
import com.example.erm_demo.domain.enums.Origin;
import com.example.erm_demo.domain.enums.TypeERM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CauseRepository  extends JpaRepository<Cause,Long> {

    @Query("SELECT c FROM Cause c " +
            "WHERE (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:type IS NULL OR c.type = :type) " +
            "AND (:origin IS NULL OR c.origin = :origin) " +
            "AND (:isStatus IS NULL OR c.isActive = :isStatus)")
    Page<Cause> search(
            @Param("keyword") String keyword,
            @Param("type") String type,
            @Param("origin") String origin,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );


}
