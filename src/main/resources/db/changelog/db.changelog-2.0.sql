--liquibase formatted sql

--changeset ilya_shulenin:1
INSERT INTO worker (email, name, surname)  VALUES ('jhon@mainl.com', 'Jhon', 'Jhonson');
INSERT INTO worker (email, name, surname)  VALUES ('smith@mainl.com', 'Smith', 'Smithson');
INSERT INTO worker (email, name, surname)  VALUES ('jack@mainl.com', 'Jack', 'Jackson');
INSERT INTO worker (email, name, surname)  VALUES ('ariana@mainl.com', 'Ariana', 'Grande');
INSERT INTO worker (email, name, surname)  VALUES ('tacker@mainl.com', 'Tacker', 'Karlson');

--changeset ilya_shulenin:2
INSERT INTO product (name, measure)  VALUES ('Milk', 'LITER');
INSERT INTO product (name, measure)  VALUES ('Eggs', 'UNIT');
INSERT INTO product (name, measure)  VALUES ('Apples', 'KG');
INSERT INTO product (name, measure)  VALUES ('Strawberry', 'KG');
INSERT INTO product (name, measure)  VALUES ('Peaches', 'KG');
INSERT INTO product (name, measure)  VALUES ('Seed', 'KG');

