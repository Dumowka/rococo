create extension if not exists "uuid-ossp";

create table if not exists "user"
(
    id        UUID unique        not null default uuid_generate_v4() primary key,
    username  varchar(50) unique not null,
    firstname varchar(255),
    lastname  varchar(255),
    avatar    bytea
);