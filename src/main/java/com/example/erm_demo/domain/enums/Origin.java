package com.example.erm_demo.domain.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Enum cho nguồn gốc (Origin)
 * Định nghĩa các loại nguồn gốc có thể có trong hệ thống
 */
@Getter
public enum Origin {
    EXTERNAL("EXTERNAL", "External origin"),// Nguồn gốc bên ngoài
    INTERNAL("INTERNAL", "Internal origin");// Nguồn gốc nội bộ

    private final String displayName;
    private final String description;

    // Static HashMap để tối ưu hóa tốc độ truy vấn - O(1) thay vì O(n)
    private static final Map<String, Origin> DISPLAY_NAME_MAP = new HashMap<>();

    // Static block để khởi tạo HashMap khi class được load
    static {
        for (Origin origin : values()) {
            DISPLAY_NAME_MAP.put(origin.displayName, origin);
        }
    }

    Origin(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Tìm enum theo displayName với tốc độ O(1)
     * @param displayName tên hiển thị
     * @return Origin enum hoặc null nếu không tìm thấy
     */
    public static Origin fromDisplayName(String displayName) {
        return DISPLAY_NAME_MAP.get(displayName);
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
