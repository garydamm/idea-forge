create table if not exists users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username varchar(256),
  password varchar(256),
  enabled boolean
);

create table if not exists authorities (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username varchar(256),
  authority varchar(256)
);