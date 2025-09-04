package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RiskCategoryRepository extends JpaRepository<RiskCategoryEntity,Long>, JpaSpecificationExecutor<RiskCategoryEntity> {

//    Tìm kiếm RiskCategory với nhiều tiêu chí
//    @Query("SELECT DISTINCT rc FROM RiskCategoryEntity rc " +
//            "LEFT JOIN RiskCategoryMapEntity rcm ON rc.id = rcm.riskCategoryId " +
//            "WHERE (:keyword IS NULL OR LOWER(rc.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(rc.code) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
//            "AND (:systemId IS NULL OR rcm.systemId = :systemId) " +
//            "AND (:isActive IS NULL OR rc.isActive = :isActive)")
//    Page<RiskCategoryEntity> searchBy(@Param("keyword") String keyword,
//                                      @Param("systemId") Long systemId,
//                                      @Param("isActive") Boolean isActive,
//                                      Pageable pageable);
//
//
}
