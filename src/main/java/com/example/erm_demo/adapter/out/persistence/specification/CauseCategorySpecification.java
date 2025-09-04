package com.example.erm_demo.adapter.out.persistence.specification;

import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryMapEntity;
import org.springframework.data.jpa.domain.Specification;

public class CauseCategorySpecification {

    /**
     * Tìm kiếm theo keyword (code hoặc name) - sử dụng BaseSpecification
     */
    public static Specification<CauseCategoryEntity> hasKeyword(String keyword) {
        return BaseSpecification.hasKeyword(keyword);
    }

    /**
     * Tìm kiếm theo systemId thông qua bảng mapping - sử dụng BaseSpecification
     */
    public static Specification<CauseCategoryEntity> hasSystemId(Long systemId) {
        return BaseSpecification.hasRelatedId(
            systemId,
            CauseCategoryMapEntity.class,
            "causeCategoryId",
            "systemId"
        );
    }

    /**
     * Tìm kiếm theo systemId sử dụng JOIN - sử dụng BaseSpecification
     */
    public static Specification<CauseCategoryEntity> hasSystemIdWithJoin(Long systemId) {
        return BaseSpecification.hasRelatedIdWithJoin(systemId, "causeCategoryMaps", "systemId");
    }

    /**
     * Specification tổng hợp cho search
     */
    public static Specification<CauseCategoryEntity> searchCriteria(String keyword, Long systemId) {
        return Specification.where(hasKeyword(keyword))
                           .and(hasSystemId(systemId));
    }

    // Các method bổ sung sử dụng BaseSpecification

    /**
     * Tìm kiếm theo description
     */
    public static Specification<CauseCategoryEntity> hasDescription(String description) {
        return BaseSpecification.hasKeywordInField(description, "description");
    }

    /**
     * Tìm kiếm theo note
     */
    public static Specification<CauseCategoryEntity> hasNote(String note) {
        return BaseSpecification.hasKeywordInField(note, "note");
    }

    /**
     * Tìm kiếm theo code chính xác
     */
    public static Specification<CauseCategoryEntity> hasExactCode(String code) {
        return BaseSpecification.hasFieldEqual("code", code);
    }

    /**
     * Search criteria mở rộng với nhiều tham số hơn
     */
    public static Specification<CauseCategoryEntity> advancedSearchCriteria(
            String keyword,
            Long systemId,
            String description,
            String note) {
        return Specification.where(hasKeyword(keyword))
                           .and(hasSystemId(systemId))
                           .and(hasDescription(description))
                           .and(hasNote(note));
    }
}
