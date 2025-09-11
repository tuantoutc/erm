--liquibase formatted sql
--changeset author:add-risk-file-fields

-- Add type and created_at fields to risk_files table
ALTER TABLE `risk_files` ADD COLUMN `type` varchar(50);
ALTER TABLE `risk_files` ADD COLUMN `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

