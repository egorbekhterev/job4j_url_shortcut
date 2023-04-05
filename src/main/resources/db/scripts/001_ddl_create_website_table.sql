create table website
(
    id serial primary key NOT NULL,
    domain_name text NOT NULL UNIQUE,
    login text NOT NULL UNIQUE,
    password text NOT NULL
);
