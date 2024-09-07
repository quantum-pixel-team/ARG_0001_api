package com.quantum_pixel.arg.event.specification;

import com.quantum_pixel.arg.event.model.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventSpecification {
    private EventSpecification() {
    }

    private static final String START_TIME = "startTime";
    private static final String START_DATE = "startDate";
    private static final String LANGUAGE = "language";

    public static Specification<Event> isUpcomingEvent() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(START_DATE), LocalDate.now()),
                        criteriaBuilder.or(criteriaBuilder.isNull(root.get(START_TIME)),
                                criteriaBuilder.greaterThanOrEqualTo(root.get(START_TIME), LocalTime.now()),
                                criteriaBuilder.greaterThan(root.get(START_DATE), LocalDate.now()))
                );
    }

    public static Specification<Event> isRecentEvent() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get(START_DATE), LocalDate.now()),
                        criteriaBuilder.or(
                                criteriaBuilder.isNull(root.get(START_TIME)),
                                criteriaBuilder.lessThan(root.get(START_TIME), LocalTime.now()),
                                criteriaBuilder.lessThan(root.get(START_DATE), LocalDate.now())
                        )
                );
    }
    public static Specification<Event> language(String language) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get(LANGUAGE), language));
    }
    public static Specification<Event> orderStartDateDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get(START_DATE)));
            return null;
        };
    }
}
