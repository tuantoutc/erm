package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RiskCategoryRepository extends JpaRepository<RiskCategoryEntity,Long> {

    // Tìm kiếm RiskCategory với nhiều tiêu chí
    @Query("SELECT DISTINCT rc FROM RiskCategoryEntity rc " +
            "LEFT JOIN RiskCategoryMapEntity rcm ON rc.id = rcm.riskCategoryId " +
            "WHERE (:keyword IS NULL OR LOWER(rc.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(rc.code) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:systemId IS NULL OR rcm.systemId = :systemId) " +
            "AND (:isActive IS NULL OR rc.isActive = :isActive)")
    Page<RiskCategoryEntity> searchBy(@Param("keyword") String keyword,
                                      @Param("systemId") Long systemId,
                                      @Param("isActive") Boolean isActive,
                                      Pageable pageable);

    // Tìm tất cả RiskCategory con theo parent ID
    @Query("SELECT rc FROM RiskCategoryEntity rc WHERE rc.parentId = :parentId")
    List<RiskCategoryEntity> findByParentId(@Param("parentId") Long parentId);

    // Tìm tất cả RiskCategory root (không có parent)
    @Query("SELECT rc FROM RiskCategoryEntity rc WHERE rc.parentId IS NULL")
    List<RiskCategoryEntity> findRootCategories();

    // Tìm RiskCategory theo code
    @Query("SELECT rc FROM RiskCategoryEntity rc WHERE rc.code = :code")
    RiskCategoryEntity findByCode(@Param("code") String code);

    // Kiểm tra xem có RiskCategory con nào không
    @Query("SELECT COUNT(rc) FROM RiskCategoryEntity rc WHERE rc.parentId = :parentId")
    Long countByParentId(@Param("parentId") Long parentId);

    // Tìm tất cả RiskCategory theo systemId
    @Query("SELECT DISTINCT rc FROM RiskCategoryEntity rc " +
            "LEFT JOIN RiskCategoryMapEntity rcm ON rc.id = rcm.riskCategoryId " +
            "WHERE rcm.systemId = :systemId")
    List<RiskCategoryEntity> findBySystemId(@Param("systemId") Long systemId);
}
