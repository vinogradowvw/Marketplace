INSERT INTO users (id_user, email, password, username)
VALUES (nextval('users_seq'), 'testmail01@gmail.com' , '$2a$12$jUHXHNjA0vZY/EogSnrAbuyekA8ziMzLJ5SKSaoxJ0OnLyNKfYAXm', 'testuser01');

INSERT INTO cart (id_cart, id_user, timestamp)
VALUES (nextval('cart_seq'), 1, '2021-01-01 00:00:00');

INSERT INTO role (id_role, name)
VALUES (nextval('role_seq'), 'ROLE_ADMIN'),
       (nextval('role_seq'), 'ROLE_MODERATOR'),
       (nextval('role_seq'), 'ROLE_SELLER'),
       (nextval('role_seq'), 'ROLE_USER'),
       (nextval('role_seq'), 'ROLE_DEVELOPER'),
       (nextval('role_seq'), 'ROLE_SUPPORT_AGENT'),
       (nextval('role_seq'), 'ROLE_CONTENT_MANAGER');

INSERT INTO roles (id_role, id_user)
VALUES (1, 1),
       (2, 1),
       (5, 1);