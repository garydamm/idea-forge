alter table idea add column created_at timestamp default current_timestamp;
alter table role add column created_at timestamp default current_timestamp;
alter table user add column created_at timestamp default current_timestamp;
alter table user_role add column created_at timestamp default current_timestamp;

alter table idea add column updated_at timestamp default current_timestamp on update current_timestamp;
alter table role add column updated_at timestamp default current_timestamp on update current_timestamp;
alter table user add column updated_at timestamp default current_timestamp on update current_timestamp;
alter table user_role add column updated_at timestamp default current_timestamp on update current_timestamp;