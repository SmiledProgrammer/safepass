DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT NOT NULL,
    password VARCHAR(60) NOT NULL,
    username VARCHAR(32) NOT NULL,
    PRIMARY KEY (id)
);
