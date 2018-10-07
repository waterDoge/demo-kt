CREATE TABLE user(
  id CHAR(64) PRIMARY KEY ,
  username VARCHAR(64) NOT NULL ,
  nickname VARCHAR(64) NOT NULL DEFAULT '',
  password VARCHAR(128) NOT NULL ,
  salt CHAR(32),
  created_time TIMESTAMP NOT NULL DEFAULT current_timestamp,
  updated_time TIMESTAMP,
  algorithm TINYINT,
  UNIQUE uni_username(username)
) ENGINE innodb CHARSET utf8mb4