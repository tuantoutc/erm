package com.example.erm_demo.domain.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Enum cho loại ERM (Enterprise Risk Management)
 * Định nghĩa các loại có thể có: Rủi ro, Sự cố
 */
@Getter
public enum TypeERM {
    RISK("RISK", "Rủi ro", "Risk type"),
    INCIDENT("INCIDENT", "Sự cố", "Incident type");

    private final String code;
    private final String displayName;
    private final String description;

    // Static HashMap để tối ưu hóa tốc độ truy vấn - O(1) thay vì O(n)
    private static final Map<String, TypeERM> CODE_MAP = new HashMap<>();
    private static final Map<String, TypeERM> DISPLAY_NAME_MAP = new HashMap<>();

    // Static block để khởi tạo HashMap khi class được load
    static {
        for (TypeERM type : values()) {
            CODE_MAP.put(type.code, type);
            DISPLAY_NAME_MAP.put(type.displayName, type);
        }
    }

    TypeERM(String code, String displayName, String description) {
        this.code = code;
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Tìm enum theo code với tốc độ O(1)
     * @param code mã code
     * @return TypeERM enum hoặc null nếu không tìm thấy
     */
    public static TypeERM fromCode(String code) {
        return CODE_MAP.get(code);
    }

    /**
     * Tìm enum theo displayName với tốc độ O(1)
     * @param displayName tên hiển thị
     * @return TypeERM enum hoặc null nếu không tìm thấy
     */
    public static TypeERM fromDisplayName(String displayName) {
        return DISPLAY_NAME_MAP.get(displayName);
    }

    /**
     * Kiểm tra xem code có hợp lệ không với tốc độ O(1)
     * @param code mã code cần kiểm tra
     * @return true nếu hợp lệ, false nếu không
     */
    public static boolean isValidCode(String code) {
        return CODE_MAP.containsKey(code);
    }

    /**
     * Kiểm tra xem displayName có hợp lệ không với tốc độ O(1)
     * @param displayName tên hiển thị cần kiểm tra
     * @return true nếu hợp lệ, false nếu không
     */
    public static boolean isValidDisplayName(String displayName) {
        return DISPLAY_NAME_MAP.containsKey(displayName);
    }

    /**
     * Lấy tất cả code có sẵn
     * @return Set chứa tất cả code
     */
    public static Set<String> getAllCodes() {
        return CODE_MAP.keySet();
    }

    /**
     * Lấy tất cả displayName có sẵn
     * @return Set chứa tất cả displayName
     */
    public static Set<String> getAllDisplayNames() {
        return DISPLAY_NAME_MAP.keySet();
    }

    @Override
    public String toString() {
        return displayName;
    }
}
