-- liquibase formatted sql

-- changeset admin:1
CREATE TABLE roles
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);
-- rollback DROP TABLE roles

-- changeset admin:2
CREATE TABLE users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    firstname  VARCHAR(255),
    secondname VARCHAR(255),
    age        INT,
    username   VARCHAR(255),
    password   VARCHAR(255)
);
-- rollback DROP TABLE users

-- changeset admin:3
CREATE TABLE users_role
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- rollback DROP TABLE users_role





