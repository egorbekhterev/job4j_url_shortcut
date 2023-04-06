create table IF NOT EXISTS url
(
    id serial primary key NOT NULL,
    long_url text NOT NULL UNIQUE,
    short_url text NOT NULL UNIQUE,
    count int DEFAULT 0
);
