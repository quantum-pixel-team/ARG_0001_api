package com.quantum_pixel.arg.event.specification;

import com.quantum_pixel.arg.event.model.Event;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public record EventSearch(PageRequest pageRequest, String filter, String language) {
    public Specification<Event> getSpecification() {
        var list = new ArrayList<Specification<Event>>();

        if (filter.equals("upcoming")) {
            list.add(EventSpecification.isUpcomingEvent());
        }
        if (filter.equals("recent")) {
            list.add(EventSpecification.isRecentEvent());
        }

        list.add(EventSpecification.language(language));
        list.add(EventSpecification.orderStartDateDesc());

        return list.stream()
                .reduce(Specification::and)
                .get();
    }
}