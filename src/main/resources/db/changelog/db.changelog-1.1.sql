--liquibase formatted sql

--changeset ilya_shulenin:1
CREATE TABLE owner (
    id SMALLINT PRIMARY KEY DEFAULT 1,
    email VARCHAR(64),
    password VARCHAR(128)
);

--changeset ilya_shulenin:2
INSERT INTO owner (email, password) VALUES ('owner', 'owner');