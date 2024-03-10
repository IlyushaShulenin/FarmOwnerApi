----changeset ilya_shulenin:1
CREATE TABLE report(
    id BIGSERIAL,
    worker_id BIGINT,
    product_id BIGINT,
    amount FLOAT,
    date DATE,
    plan_is_completed BOOLEAN,

    PRIMARY KEY (id),
    FOREIGN KEY (worker_id) REFERENCES worker (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);