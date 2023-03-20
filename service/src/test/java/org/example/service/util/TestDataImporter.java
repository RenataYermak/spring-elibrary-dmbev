package org.example.service.util;

import lombok.experimental.UtilityClass;
import org.example.service.database.entity.Author;
import org.example.service.database.entity.Book;
import org.example.service.database.entity.Category;
import org.example.service.database.entity.Order;
import org.example.service.database.entity.OrderStatus;
import org.example.service.database.entity.OrderType;
import org.example.service.database.entity.Role;
import org.example.service.database.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDateTime;

@UtilityClass
public class TestDataImporter {

    public void importData(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            var drama = saveCategory(session, "Drama");
            var horror = saveCategory(session, "Horror");
            var detective = saveCategory(session, "Detective");

            var stephanKing = saveAuthor(session, "Stephan King");
            var conanDoyle = saveAuthor(session, "Conan Doyle");
            var edgarAllanPoe = saveAuthor(session, "Edgar Allan Poe");
            var agathaChristie = saveAuthor(session, "Agatha Christie");

            var deathOnTheNile = saveBook(session, "Death on the Nile", 1937,
                    "description", 5, detective, agathaChristie);
            var thePrematureBurial = saveBook(session, "The Premature Burial", 1977,
                    "description", 2, drama, edgarAllanPoe);
            var theMemoirsOfSherlockHolmes = saveBook(session, "The Memoirs of Sherlock Holmes", 1893,
                    "description", 7, horror, conanDoyle);
            var theShining = saveBook(session, "The Shining", 1937,
                    "description", 8, detective, stephanKing);

            var renata = saveUser(session, "Renata", "Yermak",
                    "renatayermak@gmail.com", "1212", Role.ADMIN);
            var alex = saveUser(session, "Alex", "Yermak",
                    "alex@gmail.com", "3333", Role.USER);
            var nikita = saveUser(session, "Nikita", "Shturo",
                    "nikita@gmail.com", "2222", Role.USER);
            var eva = saveUser(session, "Eva", "Shturo",
                    "eva@gmail.com", "1212", Role.USER);

            saveOrder(session, deathOnTheNile, renata, OrderStatus.RESERVED,
                    OrderType.SEASON_TICKET, LocalDateTime.of(2023, 1, 10, 8, 54));
            saveOrder(session, theShining, renata, OrderStatus.ORDERED,
                    OrderType.READING_ROOM, LocalDateTime.of(2023, 3, 4, 6, 39));
            saveOrder(session, deathOnTheNile, alex, OrderStatus.RETURNED,
                    OrderType.SEASON_TICKET, LocalDateTime.of(2013, 4, 23, 13, 40));
            saveOrder(session, thePrematureBurial, eva, OrderStatus.ORDERED,
                    OrderType.READING_ROOM, LocalDateTime.of(2018, 4, 22, 5, 24));
            saveOrder(session, theMemoirsOfSherlockHolmes, nikita, OrderStatus.REJECTED,
                    OrderType.READING_ROOM, LocalDateTime.of(2019, 8, 12, 17, 34));

            session.getTransaction().commit();
        }
    }

    private Category saveCategory(Session session, String name) {
        var category = Category.builder()
                .name(name)
                .build();
        session.save(category);

        return category;
    }

    private Author saveAuthor(Session session, String name) {
        var author = Author.builder()
                .name(name)
                .build();
        session.save(author);

        return author;
    }

    private Book saveBook(Session session,
                          String title,
                          Integer publishYear,
                          String description,
                          Integer number,
                          Category category,
                          Author author) {
        var book = Book.builder()
                .title(title)
                .author(author)
                .publishYear(publishYear)
                .category(category)
                .description(description)
                .number(number)
                .build();
        session.save(book);

        return book;
    }

    private User saveUser(Session session,
                          String firstname,
                          String lastname,
                          String email,
                          String password,
                          Role role) {
        var user = User.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(password)
                .role(role)
                .build();
        session.save(user);

        return user;
    }

    private Order saveOrder(Session session,
                            Book book,
                            User user,
                            OrderStatus status,
                            OrderType type,
                            LocalDateTime orderedDate) {
        var order = Order.builder()
                .book(book)
                .user(user)
                .status(status)
                .type(type)
                .orderedDate(orderedDate)
                .build();
        session.save(order);

        return order;
    }
}
