package com.example.erm_demo.domain.enums;


import lombok.Getter;

@Getter
public enum ErrorCode {
    ENTITY_NOT_FOUND(404, "Entity not found"),
    INVALID_REQUEST(400, "Invalid request"),
    ID_CANNOT_NULL(400, "Identifier cannot be null"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    DUPLICATE_ENTITY(409, "Duplicate entity"),
    VALIDATION_FAILED(422, "Validation failed");

    private  int code;
    private  String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
