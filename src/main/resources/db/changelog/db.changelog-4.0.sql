--liquibase formatted sql

--changeset ilya_shulenin:1
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (1, 1, 1, 100, '2023-10-12');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (2, 1, 2, 150, '2023-10-12');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (3, 1, 3, 100, '2023-11-12');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (4, 1, 5, 100, '2023-10-14');

--changeset ilya_shulenin:2
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (5, 2, 2, 112, '2023-11-1');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (6, 2, 2, 150, '2023-11-1');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (7, 2, 4, 200, '2023-11-3');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (8, 2, 5, 330, '2023-11-4');

--changeset ilya_shulenin:3
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (9, 3, 3, 100, '2024-1-5');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (10, 3, 2, 200, '2024-1-8');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (11, 3, 4, 50, '2024-1-12');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (12, 3, 3, 310, '2024-1-20');

--changeset ilya_shulenin:4
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (13, 4, 5, 101, '2024-1-17');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (14, 4, 3, 73, '2024-1-18');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (15, 4, 1, 300, '2024-1-19');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (16, 4, 5, 179, '2024-1-20');

--changeset ilya_shulenin:5
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (17, 5, 6, 101, '2024-2-1');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (18, 5, 1, 73, '2024-2-3');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (19, 5, 5, 300, '2024-2-5');
INSERT INTO plan (id, worker_id, product_id, amount, date) VALUES (20, 5, 2, 179, '2024-2-1');

--changeset ilya_shulenin:6
SELECT SETVAL('plan_id_seq', 20);
