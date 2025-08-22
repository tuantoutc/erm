--liquibase formatted sql
--changeset author:initial-schema

-- Tạo database trước khi chạy các lệnh tạo bảng
CREATE DATABASE IF NOT EXISTS erm_demo;
USE erm_demo;

CREATE TABLE IF NOT EXISTS `cause_categories` (
  `id`                     bigint         PRIMARY KEY,
  `code`                   varchar(255)   UNIQUE,
  `name`                   nvarchar(255)  UNIQUE,
  `description`            text,
  `note`                   text
);

CREATE TABLE IF NOT EXISTS `cause_categories_map` (
  `id`                     bigint      PRIMARY KEY,
  `cause_categories_id`    bigint,
  `system_id`              bigint
);

CREATE TABLE IF NOT EXISTS `causes` (
  `id`                     bigint         PRIMARY KEY,
  `code`                   varchar(255)   UNIQUE,
  `name`                   varchar(255)   UNIQUE,
  `type`                   varchar(50),
  `origin`                 varchar(50),
  `cause_category_id`      bigint,
  `note`                   text,
  `is_active`              boolean
);

CREATE TABLE IF NOT EXISTS `causes_map` (
  `id`                     bigint         PRIMARY KEY,
  `causes_id`              bigint,
  `system_id`              bigint
);

CREATE TABLE IF NOT EXISTS `risk_categories` (
  `id`                     bigint         PRIMARY KEY,
  `code`                   varchar(255)   UNIQUE,
  `name`                   varchar(255)   UNIQUE,
  `parent_id`              bigint,
  `description`            text,
  `is_active`              boolean
);

CREATE TABLE IF NOT EXISTS `risk_categories_map` (
  `id`                     bigint         PRIMARY KEY,
  `system_id`              bigint,
  `risk_category_id`       bigint
);

CREATE TABLE IF NOT EXISTS `attribute_groups` (
  `id`                     bigint         PRIMARY KEY,
  `code`                   varchar(255)   UNIQUE,
  `name`                   varchar(255)   UNIQUE,
  `type`                   varchar(255),
  `description`            text,
  `is_active`              boolean
);

CREATE TABLE IF NOT EXISTS `attributes` (
  `id`                     bigint         PRIMARY KEY,
  `code`                   varchar(255)   UNIQUE,
  `name`                   varchar(255)   UNIQUE,
  `display_type`           nvarchar(255),
  `datatype`               varchar(255),
  `attribute_group_id`     bigint,
  `description`            text,
  `is_active`              boolean
);

CREATE TABLE IF NOT EXISTS `atribute_values` (
  `id`                     bigint         PRIMARY KEY,
  `value`                  nvarchar(255),
  `attribute_id`           bigint
);

CREATE TABLE IF NOT EXISTS `preventive_measures` (
  `id`                     bigint         PRIMARY KEY,
  `code`                   varchar(255)   UNIQUE,
  `name`                   varchar(255)   UNIQUE,
  `description`            text,
  `is_active`              boolean
);

CREATE TABLE IF NOT EXISTS `risk_types` (
  `id`                     bigint         PRIMARY KEY,
  `code`                   varchar(255)   UNIQUE,
  `name`                   varchar(255)   UNIQUE,
  `origin`                 varchar(50),
  `note`                   text,
  `object`                 varchar(255),
  `is_active`              boolean
);

CREATE TABLE IF NOT EXISTS `risk_types_map` (
  `id`                     bigint         PRIMARY KEY,
  `system_id`              bigint,
  `risk_type_id`           bigint
);

CREATE TABLE IF NOT EXISTS `risk_types_attributes` (
  `id`                     bigint         PRIMARY KEY,
  `risk_type_id`           bigint,
  `attribute_group_id`     bigint,
  `attribute_id`           bigint
);

CREATE TABLE IF NOT EXISTS `risk_types_attributes_values` (
  `id`                     bigint         PRIMARY KEY,
  `risk_types_attribute_id` bigint,
  `atribute_value_id`      bigint,
  `text_value`             varchar(255)
);

CREATE TABLE IF NOT EXISTS `sample_actions` (
  `id`                     bigint         PRIMARY KEY,
  `code`                   varchar(255)   UNIQUE,
  `name`                   varchar(255)   UNIQUE,
  `type`                   varchar(50),
  `risk_type_id`           bigint,
  `incident_type_id`       bigint,
  `cause_category_id`      bigint,
  `note`                   text,
  `is_active`              boolean
);

CREATE TABLE IF NOT EXISTS `sample_actions_map` (
  `id`                     bigint         PRIMARY KEY,
  `type`                   varchar(255),
  `sample_action_id`       bigint,
  `preventive_measure_id`  bigint,
  `department_id`          bigint,
  `content`                text
);

CREATE TABLE IF NOT EXISTS `risks` (
  `id`                     bigint         PRIMARY KEY,
  `code`                   varchar(255)   UNIQUE,
  `name`                   nvarchar(255)  UNIQUE,
  `system_id`              bigint,
  `risk_type_id`           bigint,
  `risk_category_id`       bigint,
  `reporter_id`            bigint,
  `recorded_time`          date,
  `priority_level`         varchar(50),
  `description`            text,
  `expected_consequence`   text,
  `level`                  bigint,
  `point`                  bigint
);

CREATE TABLE IF NOT EXISTS `risk_attribute_line` (
  `id`                     bigint         PRIMARY KEY,
  `attribute_group_id`     bigint,
  `attribute_id`           bigint
);

CREATE TABLE IF NOT EXISTS `risk_attribute_line_values` (
  `id`                     bigint         PRIMARY KEY,
  `risk_lines_id`          bigint,
  `attribute_values_id`    bigint,
  `text_value`             varchar(255)
);

CREATE TABLE IF NOT EXISTS `risk_cause_line` (
  `id`                     bigint         PRIMARY KEY,
  `risk_id`                bigint,
  `tracking_cause_id`      bigint,
  `sample_action_id`       bigint
);

CREATE TABLE IF NOT EXISTS `tracking_causes` (
  `id`                     bigint         PRIMARY KEY,
  `cause_category_id`      bigint,
  `cause_id`               bigint,
  `count`                  bigint,
  `object_applicable_type` varchar(255),
  `state`                  varchar(255)
);

CREATE TABLE IF NOT EXISTS `tracking_causes_map` (
  `id`                     bigint         PRIMARY KEY,
  `tracking_cause_id`      bigint,
  `department_id`          bigint,
  `position_id`            bigint,
  `employee_id`            bigint,
  `product_id`             bigint,
  `parner_type`            bigint,
  `group_parner_id`        bigint,
  `parner_id`              bigint
);

CREATE TABLE IF NOT EXISTS `tracking_causes_map_fail_products` (
  `id`                     bigint         PRIMARY KEY,
  `tracking_causes_map_id` bigint,
  `dic_id`                 bigint
);

CREATE TABLE IF NOT EXISTS `risk_cause_line_action_line` (
  `id`                     bigint         PRIMARY KEY,
  `risk_cause_line_id`     bigint,
  `tracking_action_id`     bigint
);

CREATE TABLE IF NOT EXISTS `tracking_actions` (
  `id`                     bigint         PRIMARY KEY,
  `preventive_measure_id`  bigint,
  `type`                   varchar(255),
  `department_id`          bigint,
  `content`                varchar(255),
  `plan_date`              date
);

CREATE TABLE IF NOT EXISTS `related_risks` (
  `id`                     bigint         PRIMARY KEY,
  `related_risk_id`        bigint,
  `risk_id`                bigint
);

CREATE TABLE IF NOT EXISTS `tasks` (
  `id`                     bigint        PRIMARY KEY,
  `code`                   varchar(255)  UNIQUE,
  `name`                   nvarchar(255) UNIQUE,
  `parent_id`              bigint,
  `assigner_id`            bigint,
  `employee_id`            bigint,
  `start_date`             date,
  `end_date`               date,
  `type`                   varchar(255),
  `priority_level`         varchar(50),
  `is_replace_task`        boolean,
  `replace_task_id`        bigint,
  `content`                text,
  `parent_task_id`         bigint,
  `files`                  text,
  `state`                  varchar(50)
);

CREATE TABLE IF NOT EXISTS `risk_files` (
  `id`                     bigint         PRIMARY KEY,
  `name`                   varchar(255),
  `url`                    varchar(255),
  `risk_id`                bigint
);

CREATE TABLE IF NOT EXISTS `tags` (
  `id`                     bigint         PRIMARY KEY,
  `name`                   varchar(255),
  `color`                  varchar(255)
);

CREATE TABLE IF NOT EXISTS `risk_tag` (
  `id`                     bigint         PRIMARY KEY,
  `tag_id`                 bigint,
  `risk_id`                bigint
);

CREATE TABLE IF NOT EXISTS `task_tags` (
  `id`                     bigint         PRIMARY KEY,
  `tag_id`                 bigint,
  `task_id`                bigint
);

CREATE TABLE IF NOT EXISTS `task_files` (
  `id`                     bigint         PRIMARY KEY,
  `name`                   varchar(255),
  `url`                    varchar(255),
  `task_id`                bigint
);

CREATE TABLE IF NOT EXISTS `incident_categories` (
  `id`                     bigint         PRIMARY KEY,
  `code`                   varchar(255)   UNIQUE,
  `name`                   varchar(255)   UNIQUE,
  `parent_id`              bigint,
  `description`            text,
  `is_active`              boolean
);

CREATE TABLE IF NOT EXISTS `incident_categories_map` (
  `id`                     bigint         PRIMARY KEY,
  `system_id`              bigint,
  `incident_category_id`   bigint
);

CREATE TABLE IF NOT EXISTS `incident_types` (
  `id`                     bigint         PRIMARY KEY,
  `code`                   varchar(255)   UNIQUE,
  `name`                   varchar(255)   UNIQUE,
  `origin`                 varchar(50),
  `note`                   text,
  `object`                 varchar(255),
  `is_active`              boolean
);

CREATE TABLE IF NOT EXISTS `incident_types_map` (
  `id`                     bigint         PRIMARY KEY,
  `system_id`              bigint,
  `incident_type_id`       bigint
);

CREATE TABLE IF NOT EXISTS `incident_types_attributes` (
  `id`                     bigint         PRIMARY KEY,
  `incident_type_id`       bigint,
  `attribute_group_id`     bigint,
  `attribute_id`           bigint
);

CREATE TABLE IF NOT EXISTS `incident_types_attributes_values` (
  `id`                     bigint         PRIMARY KEY,
  `incident_types_attribute_id` bigint,
  `atribute_value_id`      bigint,
  `text_value`             varchar(255)
);

CREATE TABLE IF NOT EXISTS `incidents` (
  `id`                     bigint         PRIMARY KEY,
  `code`                   varchar(255)   UNIQUE,
  `name`                   nvarchar(255)  UNIQUE,
  `system_id`              bigint,
  `incident_type_id`       bigint,
  `incident_category_id`   bigint,
  `discovery_date`         date,
  `report_date`            date,
  `reporter_id`            bigint,
  `owner_id`               bigint,
  `impacted_user_id`       bigint,
  `priority_level`         varchar(50),
  `description`            text,
  `level`                  bigint,
  `point`                  bigint
);

CREATE TABLE IF NOT EXISTS `incident_attribute_line` (
  `id`                     bigint         PRIMARY KEY,
  `attribute_group_id`     bigint,
  `attribute_id`           bigint
);

CREATE TABLE IF NOT EXISTS `incident_attribute_line_values` (
  `id`                     bigint         PRIMARY KEY,
  `incident_lines_id`      bigint,
  `attribute_values_id`    bigint,
  `text_value`             varchar(255)
);

CREATE TABLE IF NOT EXISTS `incident_cause_line` (
  `id`                     bigint         PRIMARY KEY,
  `impact_des`             text,
  `indicator`              text,
  `incident_id`            bigint,
  `tracking_cause_id`      bigint,
  `temp_fix_des`           text
);

CREATE TABLE IF NOT EXISTS `incident_cause_line_action_line` (
  `id`                     bigint         PRIMARY KEY,
  `incident_cause_line_id` bigint,
  `tracking_action_id`     bigint
);

CREATE TABLE IF NOT EXISTS `related_incidents` (
  `id`                     bigint         PRIMARY KEY,
  `related_incident_id`    bigint,
  `incident_id`            bigint
);

-- Foreign Keys
ALTER TABLE `causes` ADD FOREIGN KEY (`cause_category_id`) REFERENCES `cause_categories` (`id`);

ALTER TABLE `causes_map` ADD FOREIGN KEY (`causes_id`) REFERENCES `causes` (`id`);

ALTER TABLE `risk_categories` ADD FOREIGN KEY (`parent_id`) REFERENCES `risk_categories` (`id`);

ALTER TABLE `risk_categories_map` ADD FOREIGN KEY (`risk_category_id`) REFERENCES `risk_categories` (`id`);

ALTER TABLE `attributes` ADD FOREIGN KEY (`attribute_group_id`) REFERENCES `attribute_groups` (`id`);

ALTER TABLE `atribute_values` ADD FOREIGN KEY (`attribute_id`) REFERENCES `attributes` (`id`);

ALTER TABLE `risk_types_map` ADD FOREIGN KEY (`risk_type_id`) REFERENCES `risk_types` (`id`);

ALTER TABLE `risk_types_attributes` ADD FOREIGN KEY (`risk_type_id`) REFERENCES `risk_types` (`id`);

ALTER TABLE `risk_types_attributes` ADD FOREIGN KEY (`attribute_group_id`) REFERENCES `attribute_groups` (`id`);

ALTER TABLE `risk_types_attributes` ADD FOREIGN KEY (`attribute_id`) REFERENCES `attributes` (`id`);

ALTER TABLE `risk_types_attributes_values` ADD FOREIGN KEY (`risk_types_attribute_id`) REFERENCES `risk_types_attributes` (`id`);

ALTER TABLE `risk_types_attributes_values` ADD FOREIGN KEY (`atribute_value_id`) REFERENCES `atribute_values` (`id`);

ALTER TABLE `sample_actions` ADD FOREIGN KEY (`risk_type_id`) REFERENCES `risk_types` (`id`);

ALTER TABLE `sample_actions` ADD FOREIGN KEY (`incident_type_id`) REFERENCES `incident_types` (`id`);

ALTER TABLE `sample_actions` ADD FOREIGN KEY (`cause_category_id`) REFERENCES `cause_categories` (`id`);

ALTER TABLE `sample_actions_map` ADD FOREIGN KEY (`sample_action_id`) REFERENCES `sample_actions` (`id`);

ALTER TABLE `sample_actions_map` ADD FOREIGN KEY (`preventive_measure_id`) REFERENCES `preventive_measures` (`id`);

ALTER TABLE `risks` ADD FOREIGN KEY (`risk_type_id`) REFERENCES `risk_types` (`id`);

ALTER TABLE `risks` ADD FOREIGN KEY (`risk_category_id`) REFERENCES `risk_categories` (`id`);

ALTER TABLE `risk_attribute_line` ADD FOREIGN KEY (`attribute_group_id`) REFERENCES `attribute_groups` (`id`);

ALTER TABLE `risk_attribute_line` ADD FOREIGN KEY (`attribute_id`) REFERENCES `attributes` (`id`);

ALTER TABLE `risk_attribute_line_values` ADD FOREIGN KEY (`risk_lines_id`) REFERENCES `risk_attribute_line` (`id`);

ALTER TABLE `risk_attribute_line_values` ADD FOREIGN KEY (`attribute_values_id`) REFERENCES `atribute_values` (`id`);

ALTER TABLE `risk_cause_line` ADD FOREIGN KEY (`risk_id`) REFERENCES `risks` (`id`);

ALTER TABLE `risk_cause_line` ADD FOREIGN KEY (`tracking_cause_id`) REFERENCES `tracking_causes` (`id`);

ALTER TABLE `risk_cause_line` ADD FOREIGN KEY (`sample_action_id`) REFERENCES `sample_actions` (`id`);

ALTER TABLE `tracking_causes` ADD FOREIGN KEY (`cause_category_id`) REFERENCES `cause_categories` (`id`);

ALTER TABLE `tracking_causes` ADD FOREIGN KEY (`cause_id`) REFERENCES `causes` (`id`);

ALTER TABLE `tracking_causes_map` ADD FOREIGN KEY (`tracking_cause_id`) REFERENCES `tracking_causes` (`id`);

ALTER TABLE `tracking_causes_map_fail_products` ADD FOREIGN KEY (`tracking_causes_map_id`) REFERENCES `tracking_causes_map` (`id`);

ALTER TABLE `risk_cause_line_action_line` ADD FOREIGN KEY (`risk_cause_line_id`) REFERENCES `risk_cause_line` (`id`);

ALTER TABLE `risk_cause_line_action_line` ADD FOREIGN KEY (`tracking_action_id`) REFERENCES `tracking_actions` (`id`);

ALTER TABLE `tracking_actions` ADD FOREIGN KEY (`preventive_measure_id`) REFERENCES `preventive_measures` (`id`);

ALTER TABLE `related_risks` ADD FOREIGN KEY (`related_risk_id`) REFERENCES `risks` (`id`);

ALTER TABLE `related_risks` ADD FOREIGN KEY (`risk_id`) REFERENCES `risks` (`id`);

ALTER TABLE `tasks` ADD FOREIGN KEY (`parent_id`) REFERENCES `tasks` (`id`);

ALTER TABLE `tasks` ADD FOREIGN KEY (`replace_task_id`) REFERENCES `tasks` (`id`);

ALTER TABLE `tasks` ADD FOREIGN KEY (`parent_task_id`) REFERENCES `tasks` (`id`);

ALTER TABLE `risk_files` ADD FOREIGN KEY (`risk_id`) REFERENCES `risks` (`id`);

ALTER TABLE `risk_tag` ADD FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`);

ALTER TABLE `risk_tag` ADD FOREIGN KEY (`risk_id`) REFERENCES `risks` (`id`);

ALTER TABLE `task_tags` ADD FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`);

ALTER TABLE `task_tags` ADD FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`);

ALTER TABLE `task_files` ADD FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`);

ALTER TABLE `incident_categories` ADD FOREIGN KEY (`parent_id`) REFERENCES `incident_categories` (`id`);

ALTER TABLE `incident_categories_map` ADD FOREIGN KEY (`incident_category_id`) REFERENCES `risk_categories` (`id`);

ALTER TABLE `incident_types_map` ADD FOREIGN KEY (`incident_type_id`) REFERENCES `incident_types` (`id`);

ALTER TABLE `incident_types_attributes` ADD FOREIGN KEY (`incident_type_id`) REFERENCES `incident_types` (`id`);

ALTER TABLE `incident_types_attributes` ADD FOREIGN KEY (`attribute_group_id`) REFERENCES `attribute_groups` (`id`);

ALTER TABLE `incident_types_attributes` ADD FOREIGN KEY (`attribute_id`) REFERENCES `attributes` (`id`);

ALTER TABLE `incident_types_attributes_values` ADD FOREIGN KEY (`incident_types_attribute_id`) REFERENCES `incident_types_attributes` (`id`);

ALTER TABLE `incident_types_attributes_values` ADD FOREIGN KEY (`atribute_value_id`) REFERENCES `atribute_values` (`id`);

ALTER TABLE `incidents` ADD FOREIGN KEY (`incident_type_id`) REFERENCES `incident_types` (`id`);

ALTER TABLE `incidents` ADD FOREIGN KEY (`incident_category_id`) REFERENCES `incident_categories` (`id`);

ALTER TABLE `incident_attribute_line` ADD FOREIGN KEY (`attribute_group_id`) REFERENCES `attribute_groups` (`id`);

ALTER TABLE `incident_attribute_line` ADD FOREIGN KEY (`attribute_id`) REFERENCES `attributes` (`id`);

ALTER TABLE `incident_attribute_line_values` ADD FOREIGN KEY (`incident_lines_id`) REFERENCES `incident_attribute_line` (`id`);

ALTER TABLE `incident_attribute_line_values` ADD FOREIGN KEY (`attribute_values_id`) REFERENCES `atribute_values` (`id`);

ALTER TABLE `incident_cause_line` ADD FOREIGN KEY (`incident_id`) REFERENCES `incidents` (`id`);

ALTER TABLE `incident_cause_line` ADD FOREIGN KEY (`tracking_cause_id`) REFERENCES `tracking_causes` (`id`);

ALTER TABLE `incident_cause_line_action_line` ADD FOREIGN KEY (`incident_cause_line_id`) REFERENCES `incident_cause_line` (`id`);

ALTER TABLE `incident_cause_line_action_line` ADD FOREIGN KEY (`tracking_action_id`) REFERENCES `tracking_actions` (`id`);

ALTER TABLE `related_incidents` ADD FOREIGN KEY (`related_incident_id`) REFERENCES `incidents` (`id`);

ALTER TABLE `related_incidents` ADD FOREIGN KEY (`incident_id`) REFERENCES `incidents` (`id`);
