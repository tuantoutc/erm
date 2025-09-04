package com.example.erm_demo.adapter.out.persistence.specification;

import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryMapEntity;
import org.springframework.data.jpa.domain.Specification;

public class RiskCategorySpecification {

    //Tìm kiếm theo keyword (code hoặc name) - sử dụng BaseSpecification
    public static Specification<RiskCategoryEntity> hasKeyword(String keyword) {
        return BaseSpecification.hasKeyword(keyword);
    }

    // Tìm kiếm theo systemId - giả sử RiskCategory cũng có mapping với System
    public static Specification<RiskCategoryEntity> hasSystemId(Long systemId) {
       return BaseSpecification.hasRelatedId(
               systemId,
               RiskCategoryMapEntity.class,
                "riskCategoryId",
                "systemId"
       );

        // Hoặc nếu có relationship trực tiếp:
//        return BaseSpecification.hasFieldEqual("systemId", systemId);
    }
    public static Specification<RiskCategoryEntity> hasIsActive(Boolean isActive) {
        return BaseSpecification.hasFieldBoolean("isActive", isActive);
    }

    // Advanced search với nhiều tiêu chí
    public static Specification<RiskCategoryEntity> advancedSearchCriteria(
            String keyword,
            Long systemId,
            Boolean isActive) {
        return Specification.where(hasKeyword(keyword))
                           .and(hasSystemId(systemId))
                           .and(hasIsActive(isActive));
    }
}
