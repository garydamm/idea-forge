create table team (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name varchar(256) not null,
  user_id INT not null,
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp on update current_timestamp
);