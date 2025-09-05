package com.example.erm_demo.domain.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@Getter
public enum ObjectType {
    // Đối tượng INTERNAL
    TAP_DOAN("TAP_DOAN", "Tập đoàn"),
    TO_CHUC("TO_CHUC", "Tổ chức"),
    BO_PHAN("BO_PHAN", "Bộ phận"),
    CA_NHAN("CA_NHAN", "Cá nhân"),

    // Đối tượng EXTERNAL
    KHACH_HANG_B2B("KHACH_HANG_B2B", "Khách hàng B2B"),
    KHACH_HANG_B2C("KHACH_HANG_B2C", "Khách hàng B2C"),
    KHACH_HANG_OEM("KHACH_HANG_OEM", "Khách hàng OEM"),
    NHA_CUNG_CAP("NHA_CUNG_CAP", "Nhà cung cấp"),
    DOI_TAC_VAN_CHUYEN("DOI_TAC_VAN_CHUYEN", "Đối tác vận chuyển"),
    DOI_TAC_THANH_TOAN("DOI_TAC_THANH_TOAN", "Đối tác thanh toán"),
    DOI_TAC_KHAC("DOI_TAC_KHAC", "Đối tác khác"),
    MERCHANT("MERCHANT", "Merchant"),
    DOI_THU("DOI_THU", "Đối thủ"),
    DOI_TAC_NOI_BO("DOI_TAC_NOI_BO", "Đối tác nội bộ");

    private final String code;
    private final String displayName;

    // Static HashMap để tối ưu hóa tốc độ truy vấn - O(1) thay vì O(n)
    private static final Map<String, ObjectType> CODE_MAP = new HashMap<>();

    // Static block để khởi tạo HashMap khi class được load
    static {
        for (ObjectType objectType : values()) {
            CODE_MAP.put(objectType.code, objectType);
        }
    }

    ObjectType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * Tìm enum theo code với tốc độ O(1)
     * @param code Code cần tìm
     * @return ObjectType tương ứng hoặc null nếu không tìm thấy
     */
    public static ObjectType fromCode(String code) {
        return CODE_MAP.get(code);
    }

    /**
     * Kiểm tra code có tồn tại hay không
     * @param code Code cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    public static boolean isValidCode(String code) {
        return CODE_MAP.containsKey(code);
    }
}
