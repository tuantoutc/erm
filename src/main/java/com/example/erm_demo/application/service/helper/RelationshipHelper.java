package com.example.erm_demo.application.service.helper;

import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Helper class để quản lý các mối quan hệ thủ công thay thế cho JPA annotations
 */
@Component
@RequiredArgsConstructor
public class RelationshipHelper {

    private final CauseCategoryRepository causeCategoryRepository;
    private final CauseRepository causeRepository;
    private final RiskCategoryRepository riskCategoryRepository;

    /**
     * Lấy tất cả Cause thuộc một CauseCategory
     */
    public List<CauseEntity> getCausesByCategoryId(Long categoryId) {
        return causeRepository.findByCauseCategoryId(categoryId);
    }

    /**
     * Lấy CauseCategory của một Cause
     */
    public Optional<CauseCategoryEntity> getCauseCategoryByCauseId(Long causeId) {
        Optional<CauseEntity> cause = causeRepository.findById(causeId);
        if (cause.isPresent() && cause.get().getCauseCategoryId() != null) {
            return causeCategoryRepository.findById(cause.get().getCauseCategoryId());
        }
        return Optional.empty();
    }

    /**
     * Lấy tất cả RiskCategory con của một parent
     */
    public List<RiskCategoryEntity> getChildRiskCategories(Long parentId) {
        return riskCategoryRepository.findByParentId(parentId);
    }

    /**
     * Lấy RiskCategory parent của một RiskCategory
     */
    public Optional<RiskCategoryEntity> getParentRiskCategory(Long categoryId) {
        Optional<RiskCategoryEntity> category = riskCategoryRepository.findById(categoryId);
        if (category.isPresent() && category.get().getParentId() != null) {
            return riskCategoryRepository.findById(category.get().getParentId());
        }
        return Optional.empty();
    }

    /**
     * Kiểm tra xem một RiskCategory có phải là root (không có parent) không
     */
    public boolean isRootCategory(Long categoryId) {
        Optional<RiskCategoryEntity> category = riskCategoryRepository.findById(categoryId);
        return category.isPresent() && category.get().getParentId() == null;
    }

    /**
     * Kiểm tra xem một RiskCategory có children không
     */
    public boolean hasChildren(Long categoryId) {
        return riskCategoryRepository.countByParentId(categoryId) > 0;
    }

    /**
     * Đếm số lượng Cause trong một CauseCategory
     */
    public Long countCausesInCategory(Long categoryId) {
        return causeRepository.countByCauseCategoryId(categoryId);
    }

    /**
     * Kiểm tra xem có thể xóa CauseCategory không (không có Cause nào)
     */
    public boolean canDeleteCauseCategory(Long categoryId) {
        return countCausesInCategory(categoryId) == 0;
    }

    /**
     * Kiểm tra xem có thể xóa RiskCategory không (không có children)
     */
    public boolean canDeleteRiskCategory(Long categoryId) {
        return !hasChildren(categoryId);
    }

    /**
     * Lấy tất cả RiskCategory root
     */
    public List<RiskCategoryEntity> getRootRiskCategories() {
        return riskCategoryRepository.findRootCategories();
    }

    /**
     * Validate parent-child relationship để tránh circular reference
     */
    public boolean isValidParentChild(Long childId, Long parentId) {
        if (childId.equals(parentId)) {
            return false; // Không thể là parent của chính nó
        }

        // Kiểm tra xem parentId có phải là descendant của childId không
        return !isDescendant(parentId, childId);
    }

    /**
     * Kiểm tra xem categoryId có phải là descendant của potentialAncestorId không
     */
    private boolean isDescendant(Long categoryId, Long potentialAncestorId) {
        Optional<RiskCategoryEntity> category = riskCategoryRepository.findById(categoryId);

        while (category.isPresent() && category.get().getParentId() != null) {
            if (category.get().getParentId().equals(potentialAncestorId)) {
                return true;
            }
            category = riskCategoryRepository.findById(category.get().getParentId());
        }

        return false;
    }
}
