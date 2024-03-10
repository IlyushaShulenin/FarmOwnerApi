--liquibase formatted sql

--changeset ilya_shulenin:1
ALTER TABLE worker DROP COLUMN status;