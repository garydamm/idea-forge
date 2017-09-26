create table team_idea (
  id INT AUTO_INCREMENT PRIMARY KEY,
  team_id INT not null,
  idea_id INT not null,
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp on update current_timestamp,
  CONSTRAINT unique_team_idea UNIQUE (team_id,idea_id)
);