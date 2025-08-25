package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.CauseCategory;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RiskCategoryRepository extends JpaRepository<RiskCategory,Long> {

    @Query("SELECT rc FROM RiskCategory rc " +
            "JOIN rc.riskCategoryMaps rcm " +
            "WHERE (:keyword IS NULL OR LOWER(rc.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(rc.code) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:systemId IS NULL OR rcm.systemId = :systemId) " +
            "AND (:isActive IS NULL OR rc.isActive = :isActive)")
    Page<RiskCategory> searchBy(@Param("keyword") String keyword,
                                  @Param("systemId") Long systemId,
                                  @Param("isActive") Boolean isActive,
                                  Pageable pageable);
}
