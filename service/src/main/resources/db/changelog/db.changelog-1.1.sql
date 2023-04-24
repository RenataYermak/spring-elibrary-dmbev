--liquibase formatted sql

--changeset yermakrenata:1
ALTER TABLE orders
    DROP COLUMN reserved_date;

ALTER TABLE orders
    DROP COLUMN rejected_date;