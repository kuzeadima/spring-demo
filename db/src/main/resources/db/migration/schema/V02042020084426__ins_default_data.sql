INSERT INTO t_role (id, name)
VALUES ('7c52b12d-3dcf-4ff7-805b-07848f9405c5', 'admin');

INSERT INTO t_role (id, name)
VALUES ('454ff4c5-c595-4fb1-9d3e-a6d3c89674e5', 'user');


INSERT INTO t_user(id, username, password, role_id)
VALUES ('20d52d76-efbb-4168-a11d-8bd40abf174b', 'root', '$2a$10$4tTQe0m34v03L7iZ5owlf.BQZ/oKHx2BYt0JRkgfDX8PQrVl1NR8O', '7c52b12d-3dcf-4ff7-805b-07848f9405c5');

INSERT INTO t_user(id, username, password, role_id)
VALUES ('1895cd34-c5bc-46fb-b28c-ea47da9963a8', 'user', '$2a$10$4tTQe0m34v03L7iZ5owlf.BQZ/oKHx2BYt0JRkgfDX8PQrVl1NR8O', '454ff4c5-c595-4fb1-9d3e-a6d3c89674e5');
