package com.example.erm_demo.adapter.out.persistence.specification;

import com.example.erm_demo.adapter.out.persistence.entity.CauseEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryMapEntity;
import com.example.erm_demo.domain.enums.Origin;
import org.springframework.data.jpa.domain.Specification;

public class CauseSpecification {

    //Tìm kiếm theo keyword (code hoặc name) - sử dụng BaseSpecification

    public static Specification<CauseEntity> hasKeyword(String keyword) {
        return BaseSpecification.hasKeyword(keyword);
    }
    //Tìm kiếm theo isActive (Boolean) - sử dụng BaseSpecification

    public static Specification<CauseEntity> hasIsActive(Boolean isActive) {
        return BaseSpecification.hasFieldBoolean("isActive", isActive);
    }

    //Tìm kiếm theo description
//    public static Specification<CauseEntity> hasDescription(String description) {
//        return BaseSpecification.hasKeywordInField(description, "description");
//    }

    //Tìm kiếm theo Nguồn gốc

    public static Specification<CauseEntity> hasOrigin(Origin origin) {
        return BaseSpecification.hasFieldEqual("origin", origin);
    }

    //Tìm kiếm theo CauseCategoryId

    public static Specification<CauseEntity> hasCauseCategoryId(Long causeCategoryId) {
        return BaseSpecification.hasFieldEqual("causeCategoryId", causeCategoryId);
    }

    //Search criteria mở rộng với nhiều tham số hơn
    public static Specification<CauseEntity> advancedSearchCriteria(
            String keyword,
            Long causeCategoryId,
            Origin origin,
            Boolean isActive) {
        return Specification.where(hasKeyword(keyword))
                .and(hasCauseCategoryId(causeCategoryId))
                .and(hasOrigin(origin))
                .and(hasIsActive(isActive));
    }
}
