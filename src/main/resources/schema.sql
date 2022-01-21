DROP TABLE IF EXISTS passwords;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT,
    username VARCHAR(32) NOT NULL,
    encrypted_username VARCHAR(512) NOT NULL,
    password CHAR(60) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE passwords (
    id BIGINT AUTO_INCREMENT,
    service_name VARCHAR(255) NOT NULL,
    password VARCHAR(512) NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
