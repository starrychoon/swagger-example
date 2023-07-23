CREATE TABLE user_table
(
  id IDENTITY NOT NULL PRIMARY KEY,
  login      VARCHAR(255)                NOT NULL,
  name       VARCHAR(255)                NOT NULL,
  email      VARCHAR(320)                NOT NULL,
  created_at TIMESTAMP(9) WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP(9) WITH TIME ZONE
);

CREATE UNIQUE INDEX idx_u_login ON user_table (login);
