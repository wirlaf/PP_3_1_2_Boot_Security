-- liquibase formatted sql

-- changeset admin:1
INSERT INTO roles (id, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER');

-- changeset admin:2
INSERT INTO users (id, firstname, secondname, age, username, password)
VALUES (1, 'admin', 'admin', 30, 'admin', '$2a$10$UN85.eAxwDIckmlUKq3FAe1/qjh5Hl4tv7eHRu.ndgkdG/FQqYj7G'),
       (2, 'user', 'user', 25, 'user', '$2a$10$Xcvrey.V1tpIiRpHreAD0u5nKCkcxX5soLJXUa5D8AQ7icGwkrQq2');

-- changeset admin:3
INSERT INTO users_role (user_id, role_id)
VALUES (1, 1),
       (2, 2);