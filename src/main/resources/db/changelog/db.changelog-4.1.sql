--liquibase formatted sql

--changeset ilya_shulenin:1
INSERT INTO score (id, worker_id, score, date) VALUES (1, 1, 6, '2023-10-12');
INSERT INTO score (id, worker_id, score, date) VALUES (2, 1, 4, '2023-10-12');
INSERT INTO score (id, worker_id, score, date) VALUES (3, 1, 8, '2023-11-12');
INSERT INTO score (id, worker_id, score, date) VALUES (4, 1, 9, '2023-10-14');

--changeset ilya_shulenin:2
INSERT INTO score (id, worker_id, score, date) VALUES (5, 2, 7, '2023-11-1');
INSERT INTO score (id, worker_id, score, date) VALUES (6, 2, 9, '2023-11-1');
INSERT INTO score (id, worker_id, score, date) VALUES (7, 2, 6, '2023-11-3');
INSERT INTO score (id, worker_id, score, date) VALUES (8, 2, 8, '2023-11-4');

--changeset ilya_shulenin:3
INSERT INTO score (id, worker_id, score, date) VALUES (9, 3, 8, '2024-1-5');
INSERT INTO score (id, worker_id, score, date) VALUES (10, 3, 7, '2024-1-8');
INSERT INTO score (id, worker_id, score, date) VALUES (11, 3, 10, '2024-1-12');
INSERT INTO score (id, worker_id, score, date) VALUES (12, 3, 7, '2024-1-20');

--changeset ilya_shulenin:4
INSERT INTO score (id, worker_id, score, date) VALUES (13, 4, 9, '2024-1-17');
INSERT INTO score (id, worker_id, score, date) VALUES (14, 4, 4, '2024-1-18');
INSERT INTO score (id, worker_id, score, date) VALUES (15, 4, 7, '2024-1-19');
INSERT INTO score (id, worker_id, score, date) VALUES (16, 4, 6, '2024-1-20');

--changeset ilya_shulenin:5
INSERT INTO score (id, worker_id, score, date) VALUES (17, 5, 5, '2024-2-1');
INSERT INTO score (id, worker_id, score, date) VALUES (18, 5, 8, '2024-2-3');
INSERT INTO score (id, worker_id, score, date) VALUES (19, 5, 8, '2024-2-5');
INSERT INTO score (id, worker_id, score, date) VALUES (20, 5, 9, '2024-2-1');

--changeset ilya_shulenin:6
SELECT SETVAL('score_id_seq', 20);
