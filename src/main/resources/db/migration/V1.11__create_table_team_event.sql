create table team_event (
  id INT AUTO_INCREMENT PRIMARY KEY,
  team_id INT not null,
  event_id INT not null,
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp on update current_timestamp,
  CONSTRAINT unique_team_event UNIQUE (team_id,event_id)
);