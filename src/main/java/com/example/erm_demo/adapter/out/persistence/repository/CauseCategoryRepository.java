package com.example.erm_demo.adapter.out.persistence.repository;

import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CauseCategoryRepository extends JpaRepository<CauseCategoryEntity,Long>, JpaSpecificationExecutor<CauseCategoryEntity> {

//    // Giữ lại method cũ cho backward compatibility (có thể xóa sau)
//    @Query("SELECT DISTINCT cc FROM CauseCategoryEntity cc " +
//            "INNER JOIN CauseCategoryMapEntity ccm ON cc.id = ccm.causeCategoryId " +
//            "WHERE (:keyword IS NULL OR LOWER(cc.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(cc.code) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
//            "AND (:systemId IS NULL OR ccm.systemId = :systemId)")
//    Page<CauseCategoryEntity> findAllBy(@Param("keyword") String keyword,
//                                        @Param("systemId") Long systemId,
//                                        Pageable pageable);

}
