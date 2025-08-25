package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.CauseCategory;
import com.example.erm_demo.adapter.out.persistence.repository.custom.CauseCategoryRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CauseCategoryRepository extends JpaRepository<CauseCategory,Long> , CauseCategoryRepositoryCustom {

    @Query("SELECT cc FROM CauseCategory cc " +
            "JOIN cc.causeCategoryMaps ccm " +
            "WHERE (:keyword IS NULL OR LOWER(cc.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(cc.code) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:systemId IS NULL OR ccm.systemId = :systemId)")
    Page<CauseCategory> findAllBy(@Param("keyword") String keyword,
                                  @Param("systemId") Long systemId,
                                  Pageable pageable);


}
