create table if not exists users (
  username varchar(256),
  password varchar(256),
  enabled boolean
);

create table if not exists authorities (
  username varchar(256),
  authority varchar(256)
);