--liquibase formatted sql

--changeset yermakrenata:1
CREATE TABLE category
(
    id   serial PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);
--rollback DROP TABLE IF EXISTS category

--changeset yermakrenata:2
CREATE TABLE author
(
    id   bigserial PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);
--rollback DROP TABLE IF EXISTS author

--changeset yermakrenata:3
CREATE TABLE book
(
    id           bigserial PRIMARY KEY,
    title        VARCHAR(50) NOT NULL,
    author_id    bigint      NOT NULL REFERENCES author,
    publish_year INTEGER,
    category_id  INTEGER     NOT NULL REFERENCES category,
    description  text,
    number       INTEGER     NOT NULL,
    picture      VARCHAR(128)
);
--rollback DROP TABLE IF EXISTS book

--changeset yermakrenata:4
CREATE TABLE users
(
    id        bigserial PRIMARY KEY,
    firstname VARCHAR(25) NOT NULL,
    lastname  VARCHAR(25) NOT NULL,
    email     VARCHAR(50) NOT NULL UNIQUE,
    password  VARCHAR(49) NOT NULL,
    role      VARCHAR(10) NOT NULL
);
--rollback DROP TABLE IF EXISTS users

--changeset yermakrenata:5
CREATE TABLE orders
(
    id            bigserial PRIMARY KEY,
    book_id       bigint      NOT NULL REFERENCES book,
    user_id       bigint      NOT NULL REFERENCES users,
    status        VARCHAR(20) NOT NULL,
    type          VARCHAR(20) NOT NULL,
    ordered_date  TIMESTAMP   NOT NULL,
    reserved_date TIMESTAMP,
    returned_date TIMESTAMP,
    rejected_date TIMESTAMP
);
--rollback DROP TABLE IF EXISTS orders
