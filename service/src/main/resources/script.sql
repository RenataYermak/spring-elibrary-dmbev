CREATE TABLE category
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE author
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE book
(
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(50) NOT NULL,
    author_id    BIGINT      NOT NULL REFERENCES author,
    publish_year INTEGER,
    category_id  INTEGER     NOT NULL REFERENCES category,
    description  TEXT,
    number       INTEGER     NOT NULL,
    picture      VARCHAR(128)
);

CREATE TABLE users
(
    id        BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(25) NOT NULL,
    lastname  VARCHAR(25) NOT NULL,
    email     VARCHAR(50) NOT NULL UNIQUE,
    password  VARCHAR(49) NOT NULL,
    role      VARCHAR(10) NOT NULL
);

CREATE TABLE orders
(
    id            BIGSERIAL PRIMARY KEY,
    book_id       BIGINT      NOT NULL REFERENCES book,
    user_id       BIGINT      NOT NULL REFERENCES users,
    status        VARCHAR(20) NOT NULL,
    type          VARCHAR(20) NOT NULL,
    ordered_date  TIMESTAMP   NOT NULL,
    reserved_date TIMESTAMP,
    returned_date TIMESTAMP,
    rejected_date TIMESTAMP
);

