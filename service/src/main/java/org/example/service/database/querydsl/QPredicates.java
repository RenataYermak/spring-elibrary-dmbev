package org.example.service.database.querydsl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.service.database.entity.QOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicates {

    private final List<Predicate> predicates = new ArrayList<>();

    public static QPredicates builder() {
        return new QPredicates();
    }

    public <T> QPredicates add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }
   

    public Predicate build() {
        return Optional.ofNullable(ExpressionUtils.allOf(predicates))
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }

    public QPredicates add(Object o, Long userId) {
        if (o != null) {
            if (o instanceof String) {
                predicates.add(QOrder.order.user.email.eq((String) o));
            } else if (o instanceof Long) {
                predicates.add(QOrder.order.user.id.eq((Long) o));
            }
        }
        if (userId != null) {
            predicates.add(QOrder.order.user.id.eq(userId));
        }
        return this;
    }
}
