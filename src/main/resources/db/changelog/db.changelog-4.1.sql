--liquibase formatted sql

--changeset ilya_shulenin:1
INSERT INTO score (worker_id, score, date) VALUES (1, 6, '2023-10-12');
INSERT INTO score (worker_id, score, date) VALUES (1, 4, '2023-10-12');
INSERT INTO score (worker_id, score, date) VALUES (1, 8, '2023-11-12');
INSERT INTO score (worker_id, score, date) VALUES (1, 9, '2023-10-14');

--changeset ilya_shulenin:2
INSERT INTO score (worker_id, score, date) VALUES (2, 7, '2023-11-1');
INSERT INTO score (worker_id, score, date) VALUES (2, 9, '2023-11-1');
INSERT INTO score (worker_id, score, date) VALUES (2, 6, '2023-11-3');
INSERT INTO score (worker_id, score, date) VALUES (2, 8, '2023-11-4');

--changeset ilya_shulenin:3
INSERT INTO score (worker_id, score, date) VALUES (3, 8, '2024-1-5');
INSERT INTO score (worker_id, score, date) VALUES (3, 7, '2024-1-8');
INSERT INTO score (worker_id, score, date) VALUES (3, 10, '2024-1-12');
INSERT INTO score (worker_id, score, date) VALUES (3, 7, '2024-1-20');

--changeset ilya_shulenin:4
INSERT INTO score (worker_id, score, date) VALUES (4, 9, '2024-1-17');
INSERT INTO score (worker_id, score, date) VALUES (4, 4, '2024-1-18');
INSERT INTO score (worker_id, score, date) VALUES (4, 7, '2024-1-19');
INSERT INTO score (worker_id, score, date) VALUES (4, 6, '2024-1-20');

--changeset ilya_shulenin:5
INSERT INTO score (worker_id, score, date) VALUES (5, 5, '2024-2-1');
INSERT INTO score (worker_id, score, date) VALUES (5, 8, '2024-2-3');
INSERT INTO score (worker_id, score, date) VALUES (5, 8, '2024-2-5');
INSERT INTO score (worker_id, score, date) VALUES (5, 9, '2024-2-1');