create table team_member (
  id INT AUTO_INCREMENT PRIMARY KEY,
  team_id INT not null,
  user_id INT not null,
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp on update current_timestamp
);