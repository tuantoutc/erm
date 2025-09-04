package com.example.erm_demo.domain.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public enum SourceType {
    BUSINESS("BUSINESS", "Business"),
    SYSTEM("SYSTEM", "System");

    private final String displayName;
    private final String description;

    // Static HashMap để tối ưu hóa tốc độ truy vấn - O(1) thay vì O(n)
    private static final Map<String, SourceType> DISPLAY_NAME_MAP = new HashMap<>();

    // Static block để khởi tạo HashMap khi class được load
    static {
        for (SourceType sourceType : values()) {
            DISPLAY_NAME_MAP.put(sourceType.displayName, sourceType);
        }
    }

    SourceType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public static SourceType fromDisplayName(String displayName) {
        return DISPLAY_NAME_MAP.get(displayName);
    }

    public static boolean isValidDisplayName(String displayName) {
        return DISPLAY_NAME_MAP.containsKey(displayName);
    }

    public static Set<String> getAllDisplayNames() {
        return DISPLAY_NAME_MAP.keySet();
    }

    @Override
    public String toString() {
        return displayName;
    }
}
