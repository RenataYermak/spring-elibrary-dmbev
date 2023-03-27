INSERT INTO category (id, name)
VALUES (1, 'Drama'),
       (2, 'Horror'),
       (3, 'Detective');
SELECT setval('category_id_seq', (SELECT MAX(id) FROM category));

INSERT INTO author (id, name)
VALUES (1, 'Stephan King'),
       (2, 'Conan Doyle'),
       (3, 'Edgar Allan Poe'),
       (4, 'Agatha Christie');
SELECT setval('author_id_seq', (SELECT MAX(id) FROM author));

INSERT INTO book (id, title, description, number, picture, publish_year, author_id, category_id)
VALUES (1, 'Death on the Nile', 'description', 5, 'picture', 1937,
        (SELECT id FROM author WHERE name = 'Agatha Christie'),
        (SELECT id FROM category WHERE name = 'Detective')),
       (2, 'The Premature Burial', 'description', 2, 'picture', 1977,
        (SELECT id FROM author WHERE name = 'Edgar Allan Poe'),
        (SELECT id FROM category WHERE name = 'Drama')),
       (3, 'The Memoirs of Sherlock Holmes', 'description', 7, 'picture', 1893,
        (SELECT id FROM author WHERE name = 'Conan Doyle'),
        (SELECT id FROM category WHERE name = 'Horror')),
       (4, 'The Shining', 'description', 8, 'picture', 1937, (SELECT id FROM author WHERE name = 'Stephan King'),
        (SELECT id FROM category WHERE name = 'Detective'));
SELECT setval('book_id_seq', (SELECT MAX(id) FROM book));

INSERT INTO users (id, email, firstname, lastname, password, role)
VALUES (1, 'renatayermak@gmail.com', 'Renata', 'Yermak', '1212', 'ADMIN'),
       (2, 'alex@gmail.com', 'Alex', 'Yermak', '3333', 'USER'),
       (3, 'nikita@gmail.com', 'Nikita', 'Shturo', '2222', 'USER'),
       (4, 'eva@gmail.com', 'Eva', 'Shturo', '1212', 'USER');
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO orders (id, ordered_date, status, type, book_id, user_id)
VALUES (1, '2023-01-10 8:54', 'RESERVED', 'SEASON_TICKET', (SELECT id FROM book WHERE title = 'Death on the Nile'),
        (SELECT id FROM users WHERE email = 'renatayermak@gmail.com')),
       (2, '2023-03-04 6:54', 'ORDERED', 'READING_ROOM', (SELECT id FROM book WHERE title = 'The Shining'),
        (SELECT id FROM users WHERE email = 'renatayermak@gmail.com')),
       (3, '2023-04-23 13:54', 'RETURNED', 'SEASON_TICKET', (SELECT id FROM book WHERE title = 'Death on the Nile'),
        (SELECT id FROM users WHERE email = 'alex@gmail.com')),
       (4, '2018-04-22 5:24', 'ORDERED', 'READING_ROOM', (SELECT id FROM book WHERE title = 'The Premature Burial'),
        (SELECT id FROM users WHERE email = 'eva@gmail.com')),
       (5, '2023-08-12 17:54', 'REJECTED', 'READING_ROOM',
        (SELECT id FROM book WHERE title = 'The Memoirs of Sherlock Holmes'),
        (SELECT id FROM users WHERE email = 'nikita@gmail.com'));
SELECT setval('orders_id_seq', (SELECT MAX(id) FROM orders));