ALTER TABLE t_user
ADD CONSTRAINT fk_user_role
FOREIGN KEY (role_id) REFERENCES t_role(id);

ALTER TABLE t_publication
ADD CONSTRAINT fk_publication_user
FOREIGN KEY (published_by) REFERENCES t_user(id);
