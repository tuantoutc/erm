package com.example.erm_demo.adapter.out.persistence.specification;

import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryEntity;
import org.springframework.data.jpa.domain.Specification;

public class RiskCategorySpecification {

    /**
     * Tìm kiếm theo keyword (code hoặc name) - sử dụng BaseSpecification
     */
    public static Specification<RiskCategoryEntity> hasKeyword(String keyword) {
        return BaseSpecification.hasKeyword(keyword);
    }

    /**
     * Tìm kiếm theo systemId - giả sử RiskCategory cũng có mapping với System
     */
    public static Specification<RiskCategoryEntity> hasSystemId(Long systemId) {
        // Giả sử có RiskCategoryMapEntity tương tự
        // return BaseSpecification.hasRelatedId(
        //     systemId,
        //     RiskCategoryMapEntity.class,
        //     "riskCategoryId",
        //     "systemId"
        // );

        // Hoặc nếu có relationship trực tiếp:
        return BaseSpecification.hasFieldEqual("systemId", systemId);
    }

    /**
     * Tìm kiếm theo description
     */
    public static Specification<RiskCategoryEntity> hasDescription(String description) {
        return BaseSpecification.hasKeywordInField(description, "description");
    }

    /**
     * Tìm kiếm theo status (active/inactive)
     */
    public static Specification<RiskCategoryEntity> isActive(Boolean isActive) {
        return BaseSpecification.hasFieldEqual("isActive", isActive);
    }

    /**
     * Tìm kiếm theo exact code
     */
    public static Specification<RiskCategoryEntity> hasExactCode(String code) {
        return BaseSpecification.hasFieldEqual("code", code);
    }

    /**
     * Tìm kiếm theo multiple codes
     */
    public static Specification<RiskCategoryEntity> hasCodesIn(java.util.List<String> codes) {
        return BaseSpecification.hasFieldIn("code", codes);
    }

    /**
     * Specification tổng hợp cho search
     */
    public static Specification<RiskCategoryEntity> searchCriteria(String keyword, Long systemId, Boolean isActive) {
        return Specification.where(hasKeyword(keyword))
                           .and(hasSystemId(systemId))
                           .and(isActive(isActive));
    }

    /**
     * Advanced search với nhiều tiêu chí
     */
    public static Specification<RiskCategoryEntity> advancedSearchCriteria(
            String keyword,
            Long systemId,
            String description,
            Boolean isActive,
            java.util.List<String> codes) {
        return Specification.where(hasKeyword(keyword))
                           .and(hasSystemId(systemId))
                           .and(hasDescription(description))
                           .and(isActive(isActive))
                           .and(hasCodesIn(codes));
    }
}
