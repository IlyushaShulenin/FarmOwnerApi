--liquibase formatted sql

--changeset ilya_shulenin:1
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (1, 1, 1, 100, '2023-10-12', true);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (2, 1, 2, 123, '2023-10-12', false);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (3, 1, 3, 101, '2023-11-12', true);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (4, 1, 5, 100, '2023-10-14', true);

--changeset ilya_shulenin:2
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (5, 2, 2, 110, '2023-11-1', false);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (6, 2, 2, 120, '2023-11-1', false);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (7, 2, 4, 200, '2023-11-3', true);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (8, 2, 5, 330, '2023-11-4', true);

--changeset ilya_shulenin:3
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (9, 3, 3, 100, '2024-1-5', true);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (10, 3, 2, 200, '2024-1-8', true);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (11, 3, 4, 50, '2024-1-12', true);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (12, 3, 3, 310, '2024-1-20', true);

--changeset ilya_shulenin:4
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (13, 4, 5, 79, '2024-1-17', false);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (14, 4, 3, 73, '2024-1-18', true);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (15, 4, 1, 300, '2024-1-19', true);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (16, 4, 5, 179, '2024-1-20', true);

--changeset ilya_shulenin:5
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (17, 5, 6, 101, '2024-2-1', true);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (18, 5, 1, 74, '2024-2-3', false);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (19, 5, 5, 278, '2024-2-5', false);
INSERT INTO report (id, worker_id, product_id, amount, date, plan_is_completed) VALUES (20, 5, 2, 179, '2024-2-1', true);

--changeset ilya_shulenin:6
SELECT SETVAL('report_id_seq', 20);
