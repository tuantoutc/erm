package com.example.erm_demo.domain.enums;


import lombok.Getter;

@Getter
public enum ErrorCode {
    ENTITY_NOT_FOUND(404, "Entity not found"),
    SYSTEM_COMPONENT_NOT_MODIFIABLE(400, "System component cannot be modified"),
    PARENT_CANNOT_BE_SELF(400, "Parent entity cannot be self"),
    INVALID_REQUEST(400, "Invalid request"),
    ID_CANNOT_NULL(400, "Identifier cannot be null"),
    ID_MUST_BE_NULL(400, "Identifier must be null for creation"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    DUPLICATE_ENTITY(409, "Duplicate entity"),
    INVALID_OBJECT_FOR_ORIGIN(400, "Invalid object for the specified origin"),
    VALIDATION_FAILED(422, "Validation failed");

    private  int code;
    private  String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
