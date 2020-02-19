ALTER TABLE t_user
ADD CONSTRAINT fk_user_role
FOREIGN KEY (role_id) REFERENCES t_role(id);
