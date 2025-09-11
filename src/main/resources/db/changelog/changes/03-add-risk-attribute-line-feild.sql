--liquibase formatted sql
--changeset author:add-risk-attribute-line-field

-- Thêm trường risk_id vào bảng risk_attribute_line
ALTER TABLE `risk_attribute_line` ADD COLUMN `risk_id` bigint;

-- Thiết lập khóa ngoại đến bảng risks
ALTER TABLE `risk_attribute_line` ADD CONSTRAINT FK_risk_attribute_line_risk_id FOREIGN KEY (`risk_id`) REFERENCES `risks`(`id`);
