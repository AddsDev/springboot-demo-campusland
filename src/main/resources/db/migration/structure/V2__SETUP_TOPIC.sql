create table topics
(
    topic_id    bigint primary key auto_increment,
    title       varchar(50)    not null,
    description varchar(255)   not null,
    rating      decimal(10, 2) not null,
    views       int            not null,
    created_at  timestamp      not null default now(),
    updated_at  timestamp      not null default now(),
    category_id bigint         not null,
    foreign key (category_id) references categories (category_id)
);