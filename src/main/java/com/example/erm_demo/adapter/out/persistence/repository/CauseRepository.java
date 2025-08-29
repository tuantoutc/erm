package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.CauseEntity;
import com.example.erm_demo.domain.enums.Origin;
import com.example.erm_demo.domain.enums.TypeERM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CauseRepository extends JpaRepository<CauseEntity,Long> {

    // Tìm kiếm Cause với nhiều tiêu chí
    @Query("SELECT c FROM CauseEntity c " +
            "WHERE (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:type IS NULL OR c.type = :type) " +
            "AND (:origin IS NULL OR c.origin = :origin) " +
            "AND (:isActive IS NULL OR c.isActive = :isActive)")
    Page<CauseEntity> search(
            @Param("keyword") String keyword,
            @Param("type") TypeERM type,
            @Param("origin") Origin origin,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );

    // Tìm tất cả Cause theo CauseCategory ID
    @Query("SELECT c FROM CauseEntity c WHERE c.causeCategoryId = :causeCategoryId")
    List<CauseEntity> findByCauseCategoryId(@Param("causeCategoryId") Long causeCategoryId);

    // Tìm Cause theo code
    @Query("SELECT c FROM CauseEntity c WHERE c.code = :code")
    CauseEntity findByCode(@Param("code") String code);

    // Đếm số lượng Cause theo CauseCategory ID
    @Query("SELECT COUNT(c) FROM CauseEntity c WHERE c.causeCategoryId = :causeCategoryId")
    Long countByCauseCategoryId(@Param("causeCategoryId") Long causeCategoryId);


    void deleteByCauseCategoryId(Long id);
}
