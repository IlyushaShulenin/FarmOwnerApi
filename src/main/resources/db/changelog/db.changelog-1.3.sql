--liquibase formatted sql

--changeset ilya_shulenin:1
ALTER TABLE score DROP COLUMN plan_is_completed;