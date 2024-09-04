INSERT INTO users (id_user, email, password, username)
VALUES (1, 'testmail01@gmail.com' , '$2a$12$jUHXHNjA0vZY/EogSnrAbuyekA8ziMzLJ5SKSaoxJ0OnLyNKfYAXm', 'testuser01'),
       (2, 'testmail02@gmail.com' , '$2a$12$XHguJTHrm.6G3/aby2d3sOTtlYsJovCrcFNZrUCjGdxC6u87rAAKq', 'testuser02'),
       (3, 'testmail03@gmail.com' , '$2a$12$RN8a0moVhL64TsbNa2SayeIzLob3CAxxG6QiXO9iPQZD6OUKJZMQy', 'testuser03');

INSERT INTO cart (id_cart, id_user, timestamp)
VALUES (1, 1, '2021-01-01 00:00:00'),
       (2, 2, '2021-01-02 00:00:00'),
       (3, 3, '2021-01-03 00:00:00');

INSERT INTO role (id_role, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_MODERATOR'),
       (3, 'ROLE_SELLER'),
       (4, 'ROLE_USER'),
       (5, 'ROLE_DEVELOPER'),
       (6, 'ROLE_SUPPORT_AGENT'),
       (7, 'ROLE_CONTENT_MANAGER');

INSERT INTO roles (id_role, id_user)
VALUES (1, 1),
       (2, 2),
       (5, 2),
       (3, 3),
       (4, 3);

