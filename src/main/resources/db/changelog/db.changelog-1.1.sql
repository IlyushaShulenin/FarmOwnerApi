--liquibase formatted sql

--changeset ilya_shulenin:1
CREATE TABLE owner (
    id SMALLINT PRIMARY KEY,
    email VARCHAR(64),
    password VARCHAR(128)
);

--changeset ilya_shulenin:2
INSERT INTO owner (id, email, password) VALUES (1, 'ilya.shulenin36@gmail.com', '{bcrypt}owner');