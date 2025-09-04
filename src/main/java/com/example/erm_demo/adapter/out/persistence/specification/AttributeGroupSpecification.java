package com.example.erm_demo.adapter.out.persistence.specification;

import com.example.erm_demo.adapter.out.persistence.entity.AttributeGroupEntity;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class AttributeGroupSpecification {

    /**
     * Tìm kiếm theo keyword (code hoặc name) - sử dụng BaseSpecification
     */
    public static Specification<AttributeGroupEntity> hasKeyword(String keyword) {
        return BaseSpecification.hasKeyword(keyword);
    }

    /**
     * Tìm kiếm theo keyword trên các field tùy chỉnh
     */
    public static Specification<AttributeGroupEntity> hasKeywordInCustomFields(String keyword) {
        return BaseSpecification.hasKeywordInFields(keyword, "code", "name", "description");
    }

    /**
     * Tìm kiếm theo exact code
     */
    public static Specification<AttributeGroupEntity> hasExactCode(String code) {
        return BaseSpecification.hasFieldEqual("code", code);
    }

    /**
     * Tìm kiếm theo type
     */
    public static Specification<AttributeGroupEntity> hasType(String type) {
        return BaseSpecification.hasFieldEqual("type", type);
    }

    /**
     * Tìm kiếm theo status
     */
    public static Specification<AttributeGroupEntity> isActive(Boolean isActive) {
        return BaseSpecification.hasFieldEqual("isActive", isActive);
    }

    /**
     * Tìm kiếm theo created date range
     */
    public static Specification<AttributeGroupEntity> createdBetween(LocalDateTime from, LocalDateTime to) {
        return BaseSpecification.hasFieldBetween("createdAt", from, to);
    }

    /**
     * Tìm kiếm theo createdBy
     */
    public static Specification<AttributeGroupEntity> hasCreatedBy(Long createdBy) {
        return BaseSpecification.hasFieldEqual("createdBy", createdBy);
    }

    /**
     * Tìm kiếm records có description không null
     */
//    public static Specification<AttributeGroupEntity> hasDescription() {
//        return BaseSpecification.hasFieldNotNull("description");
//    }

    /**
     * Tìm kiếm theo multiple types
     */
    public static Specification<AttributeGroupEntity> hasTypesIn(java.util.List<String> types) {
        return BaseSpecification.hasFieldIn("type", types);
    }

    /**
     * Basic search criteria
     */
    public static Specification<AttributeGroupEntity> searchCriteria(String keyword, String type, Boolean isActive) {
        return Specification.where(hasKeyword(keyword))
                           .and(hasType(type))
                           .and(isActive(isActive));
    }

    /**
     * Advanced search với đầy đủ các tiêu chí
     */
    public static Specification<AttributeGroupEntity> advancedSearchCriteria(
            String keyword,
            String type,
            Boolean isActive,
            Long createdBy,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            java.util.List<String> types) {
        return Specification.where(hasKeywordInCustomFields(keyword))
                           .and(hasType(type))
                           .and(isActive(isActive))
                           .and(hasCreatedBy(createdBy))
                           .and(createdBetween(createdFrom, createdTo))
                           .and(hasTypesIn(types)); // Chỉ lấy những record có description
    }
}
