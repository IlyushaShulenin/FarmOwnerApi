--liquibase formatted sql

--changeset ilya_shulenin:1
INSERT INTO worker (id, email, name, surname)  VALUES (1, 'jhon@mail.com', 'Jhon', 'Jhonson');
INSERT INTO worker (id, email, name, surname)  VALUES (2, 'smith@mail.com', 'Smith', 'Smithson');
INSERT INTO worker (id, email, name, surname)  VALUES (3, 'jack@mail.com', 'Jack', 'Jackson');
INSERT INTO worker (id, email, name, surname)  VALUES (4, 'ariana@mail.com', 'Ariana', 'Grande');
INSERT INTO worker (id, email, name, surname)  VALUES (5, 'tacker@mail.com', 'Tacker', 'Karlson');

--changeset ilya_shulenin:2
SELECT SETVAL('worker_id_seq', 5);

--changeset ilya_shulenin:3
INSERT INTO product (id, name, measure)  VALUES (1, 'Milk', 'LITER');
INSERT INTO product (id, name, measure)  VALUES (2, 'Eggs', 'UNIT');
INSERT INTO product (id, name, measure)  VALUES (3, 'Apples', 'KG');
INSERT INTO product (id, name, measure)  VALUES (4, 'Strawberry', 'KG');
INSERT INTO product (id, name, measure)  VALUES (5, 'Peaches', 'KG');
INSERT INTO product (id, name, measure)  VALUES (6, 'Seed', 'KG');

--changeset ilya_shulenin:4
SELECT SETVAL('product_id_seq', 6);

