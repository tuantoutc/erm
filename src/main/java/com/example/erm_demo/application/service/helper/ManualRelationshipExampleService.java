package com.example.erm_demo.application.service.helper;

import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service example showing manual relationship management
 */
@Service
@RequiredArgsConstructor
public class ManualRelationshipExampleService {

    private final CauseCategoryRepository causeCategoryRepository;
    private final CauseRepository causeRepository;
    private final RiskCategoryRepository riskCategoryRepository;
    private final RelationshipHelper relationshipHelper;

    /**
     * Ví dụ: Tạo mới Cause và gán vào CauseCategory
     */
    public CauseEntity createCause(CauseEntity cause, Long categoryId) {
        // Kiểm tra CauseCategory có tồn tại không
        Optional<CauseCategoryEntity> category = causeCategoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new RuntimeException("CauseCategory not found: " + categoryId);
        }

        // Set foreign key thủ công
        cause.setCauseCategoryId(categoryId);

        return causeRepository.save(cause);
    }

    /**
     * Ví dụ: Lấy Cause kèm thông tin CauseCategory (Manual Join)
     */
    public CauseWithCategoryDto getCauseWithCategory(Long causeId) {
        Optional<CauseEntity> cause = causeRepository.findById(causeId);
        if (cause.isEmpty()) {
            throw new RuntimeException("Cause not found: " + causeId);
        }

        CauseWithCategoryDto result = new CauseWithCategoryDto();
        result.setCause(cause.get());

        // Manual join - lấy category thông qua foreign key
        if (cause.get().getCauseCategoryId() != null) {
            Optional<CauseCategoryEntity> category = causeCategoryRepository.findById(cause.get().getCauseCategoryId());
            category.ifPresent(result::setCategory);
        }

        return result;
    }

    /**
     * Ví dụ: Tạo RiskCategory với parent
     */
    public RiskCategoryEntity createRiskCategory(RiskCategoryEntity riskCategory, Long parentId) {
        if (parentId != null) {
            // Kiểm tra parent có tồn tại không
            Optional<RiskCategoryEntity> parent = riskCategoryRepository.findById(parentId);
            if (parent.isEmpty()) {
                throw new RuntimeException("Parent RiskCategory not found: " + parentId);
            }

            // Kiểm tra valid parent-child relationship
            if (!relationshipHelper.isValidParentChild(riskCategory.getId(), parentId)) {
                throw new RuntimeException("Invalid parent-child relationship");
            }

            riskCategory.setParentId(parentId);
        }

        return riskCategoryRepository.save(riskCategory);
    }

    /**
     * Ví dụ: Lấy RiskCategory với hierarchy (parent và children)
     */
    public RiskCategoryWithHierarchyDto getRiskCategoryWithHierarchy(Long categoryId) {
        Optional<RiskCategoryEntity> category = riskCategoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new RuntimeException("RiskCategory not found: " + categoryId);
        }

        RiskCategoryWithHierarchyDto result = new RiskCategoryWithHierarchyDto();
        result.setCategory(category.get());

        // Lấy parent thủ công
        Optional<RiskCategoryEntity> parent = relationshipHelper.getParentRiskCategory(categoryId);
        parent.ifPresent(result::setParent);

        // Lấy children thủ công
        List<RiskCategoryEntity> children = relationshipHelper.getChildRiskCategories(categoryId);
        result.setChildren(children);

        return result;
    }

    /**
     * Ví dụ: Xóa CauseCategory (kiểm tra có Cause nào không)
     */
    public void deleteCauseCategory(Long categoryId) {
        if (!relationshipHelper.canDeleteCauseCategory(categoryId)) {
            throw new RuntimeException("Cannot delete CauseCategory: it has associated Causes");
        }

        causeCategoryRepository.deleteById(categoryId);
    }

    /**
     * Ví dụ: Xóa RiskCategory (kiểm tra có children không)
     */
    public void deleteRiskCategory(Long categoryId) {
        if (!relationshipHelper.canDeleteRiskCategory(categoryId)) {
            throw new RuntimeException("Cannot delete RiskCategory: it has child categories");
        }

        riskCategoryRepository.deleteById(categoryId);
    }

    /**
     * Ví dụ: Di chuyển tất cả Cause từ category này sang category khác
     */
    public void moveCausesToAnotherCategory(Long fromCategoryId, Long toCategoryId) {
        // Kiểm tra target category có tồn tại không
        Optional<CauseCategoryEntity> toCategory = causeCategoryRepository.findById(toCategoryId);
        if (toCategory.isEmpty()) {
            throw new RuntimeException("Target CauseCategory not found: " + toCategoryId);
        }

        // Lấy tất cả Cause của category cũ
        List<CauseEntity> causes = relationshipHelper.getCausesByCategoryId(fromCategoryId);

        // Update foreign key thủ công
        for (CauseEntity cause : causes) {
            cause.setCauseCategoryId(toCategoryId);
            causeRepository.save(cause);
        }
    }

    // DTOs để trả về kết quả với relationship data
    public static class CauseWithCategoryDto {
        private CauseEntity cause;
        private CauseCategoryEntity category;

        // Getters và Setters
        public CauseEntity getCause() { return cause; }
        public void setCause(CauseEntity cause) { this.cause = cause; }
        public CauseCategoryEntity getCategory() { return category; }
        public void setCategory(CauseCategoryEntity category) { this.category = category; }
    }

    public static class RiskCategoryWithHierarchyDto {
        private RiskCategoryEntity category;
        private RiskCategoryEntity parent;
        private List<RiskCategoryEntity> children;

        // Getters và Setters
        public RiskCategoryEntity getCategory() { return category; }
        public void setCategory(RiskCategoryEntity category) { this.category = category; }
        public RiskCategoryEntity getParent() { return parent; }
        public void setParent(RiskCategoryEntity parent) { this.parent = parent; }
        public List<RiskCategoryEntity> getChildren() { return children; }
        public void setChildren(List<RiskCategoryEntity> children) { this.children = children; }
    }
}
