--liquibase formatted sql

--changeset ilya_shulenin:1
CREATE TABLE worker(
    id BIGSERIAL,
    email VARCHAR(128) UNIQUE,
    name VARCHAR(64),
    surname VARCHAR(64),
    status VARCHAR(32),

    PRIMARY KEY (id)
);

--changeset ilya_shulenin:2
CREATE TABLE product(
    id BIGSERIAL,
    name VARCHAR(128) UNIQUE,
    measure VARCHAR(32),

    PRIMARY KEY (id)
);

--changeset ilya_shulenin:3
CREATE TABLE plan(
    id BIGSERIAL,
    worker_id BIGINT,
    product_id BIGINT,
    amount FLOAT,
    date DATE,

    PRIMARY KEY (id),
    FOREIGN KEY (worker_id) REFERENCES worker (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

----changeset ilya_shulenin:4
CREATE TABLE score(
    id BIGSERIAL,
    worker_id BIGINT,
    score SMALLINT,
    plan_is_completed BOOLEAN,
    date DATE,

    PRIMARY KEY (id)
);
