package com.example.erm_demo.adapter.out.persistence.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class BaseSpecification {

    /**
     * Tìm kiếm theo keyword trên một field cụ thể
     */
    public static <T> Specification<T> hasKeywordInField(String keyword, String fieldName) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // Trả về điều kiện true (không lọc)
            }

            String likePattern = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get(fieldName)),
                likePattern
            );
        };
    }

    /**
     * Tìm kiếm theo keyword trên nhiều fields (code hoặc name)
     */
    public static <T> Specification<T> hasKeyword(String keyword) {
        return hasKeywordInFields(keyword, "code", "name");
    }

    /**
     * Tìm kiếm theo keyword trên nhiều fields tùy chỉnh
     */
    public static <T> Specification<T> hasKeywordInFields(String keyword, String... fieldNames) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // Trả về điều kiện true (không lọc)
            }

            String likePattern = "%" + keyword.toLowerCase() + "%";

            Predicate[] predicates = new Predicate[fieldNames.length];
            for (int i = 0; i < fieldNames.length; i++) {
                predicates[i] = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(fieldNames[i])),
                    likePattern
                );
            }

            return criteriaBuilder.or(predicates);
        };
    }

    /**
     * Tìm kiếm theo ID của entity liên quan thông qua bảng mapping sử dụng subquery
     */
    public static <T, M> Specification<T> hasRelatedId(Long relatedId,
                                                       Class<M> mappingEntityClass,
                                                       String mappingEntityFieldName,
                                                       String relatedIdFieldName) {
        return (root, query, criteriaBuilder) -> {
            if (relatedId == null) {
                return criteriaBuilder.conjunction(); // Trả về điều kiện true (không lọc)
            }

            // Tạo subquery để join với mapping entity
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<M> mapRoot = subquery.from(mappingEntityClass);

            subquery.select(mapRoot.get(mappingEntityFieldName))
                   .where(criteriaBuilder.equal(mapRoot.get(relatedIdFieldName), relatedId));

            return criteriaBuilder.in(root.get("id")).value(subquery);
        };
    }

    /**
     * Tìm kiếm theo ID của entity liên quan sử dụng JOIN
     */
    public static <T> Specification<T> hasRelatedIdWithJoin(Long relatedId,
                                                            String joinFieldName,
                                                            String relatedIdFieldName) {
        return (root, query, criteriaBuilder) -> {
            if (relatedId == null) {
                return criteriaBuilder.conjunction();
            }

            // Tạo join với mapping entity
            Join<T, ?> mapJoin = root.join(joinFieldName, JoinType.INNER);

            // Đảm bảo DISTINCT để tránh duplicate
            query.distinct(true);

            return criteriaBuilder.equal(mapJoin.get(relatedIdFieldName), relatedId);
        };
    }

    /**
     * Tìm kiếm theo equal value
     */
    public static <T> Specification<T> hasFieldEqual(String fieldName, Object value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(fieldName), value);
        };
    }

    /**
     * Tìm kiếm theo range (between)
     */
    public static <T> Specification<T> hasFieldBetween(String fieldName, Comparable from, Comparable to) {
        return (root, query, criteriaBuilder) -> {
            if (from == null && to == null) {
                return criteriaBuilder.conjunction();
            }

            if (from != null && to != null) {
                return criteriaBuilder.between(root.get(fieldName), from, to);
            } else if (from != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), from);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), to);
            }
        };
    }

    /**
     * Tìm kiếm theo list values (IN clause)
     */
    public static <T> Specification<T> hasFieldIn(String fieldName, java.util.List<?> values) {
        return (root, query, criteriaBuilder) -> {
            if (values == null || values.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get(fieldName).in(values);
        };
    }

    /**
     * Tìm kiếm theo boolean field
     */
    public static <T> Specification<T> hasFieldBoolean(String fieldName, Boolean value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction(); // Trả về điều kiện true (không lọc)
            }
            if (value) {
                return criteriaBuilder.isTrue(root.get(fieldName));
            } else {
                return criteriaBuilder.isFalse(root.get(fieldName));
            }
        };
    }

}
