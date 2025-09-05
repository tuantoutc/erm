package com.example.erm_demo.domain.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //@param displayName tên hiển thị @return Origin enum hoặc null nếu không tìm thấy

//    public static Origin fromDisplayName(String displayName) {
//        return DISPLAY_NAME_MAP.get(displayName);
//    }
//
//    // @param displayName tên hiển thị cần kiểm tra @return true nếu hợp lệ, false nếu không
//    public static boolean isValidDisplayName(String displayName) {
//        return DISPLAY_NAME_MAP.containsKey(displayName);
//    }
//
//    public static Set<String> getAllDisplayNames() {
//        return DISPLAY_NAME_MAP.keySet();
//    }

    /**
     * Lấy danh sách ObjectType hợp lệ cho Origin cụ thể
     * @param origin Origin cần lấy danh sách ObjectType
     * @return List chứa các ObjectType hợp lệ
     */
    public static List<ObjectType> getValidObjects(Origin origin) {
        switch (origin) {
            case INTERNAL:
                return List.of(
                    ObjectType.TAP_DOAN,
                    ObjectType.TO_CHUC,
                    ObjectType.BO_PHAN,
                    ObjectType.CA_NHAN
                );
            case EXTERNAL:
                return List.of(
                    ObjectType.KHACH_HANG_B2B,
                    ObjectType.KHACH_HANG_B2C,
                    ObjectType.KHACH_HANG_OEM,
                    ObjectType.NHA_CUNG_CAP,
                    ObjectType.DOI_TAC_VAN_CHUYEN,
                    ObjectType.DOI_TAC_THANH_TOAN,
                    ObjectType.DOI_TAC_KHAC,
                    ObjectType.MERCHANT,
                    ObjectType.DOI_THU,
                    ObjectType.DOI_TAC_NOI_BO
                );
            default:
                return List.of();
        }
    }

    public static boolean isValidObjectForOrigin(Origin origin, ObjectType objectType) {
        return getValidObjects(origin).contains(objectType);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
