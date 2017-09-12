create table user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name varchar(256),
  last_name varchar(256),
  email varchar(256),
  password varchar(256),
  active boolean
);

create table role (
  id INT AUTO_INCREMENT PRIMARY KEY,
  role varchar(256)
);

create table user_role (
  user_id INT,
  role_id INT
);

insert into role (role) values ('USER');
insert into role (role) values ('ADMIN');