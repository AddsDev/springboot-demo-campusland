create table categories
(
    category_id bigint primary key auto_increment,
    name        varchar(50) unique not null,
    created_at  timestamp      not null default now(),
    updated_at  timestamp      not null default now()
);