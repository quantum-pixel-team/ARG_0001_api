package com.quantum_pixel.arg.event.service;

import com.quantum_pixel.arg.event.model.Event;
import com.quantum_pixel.arg.event.repository.EventRepository;
import com.quantum_pixel.arg.event.specification.EventSearch;
import com.quantum_pixel.arg.event.web.mapper.EventMapper;
import com.quantum_pixel.arg.v1.web.model.EventDTO;
import com.quantum_pixel.arg.v1.web.model.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository repository;
    private final EventMapper mapper;


    public void createEvent(EventDTO eventDTO) {
        Event event = mapper.toEntity(eventDTO);
        repository.save(event);
    }

    public PageDTO getEvents(EventSearch eventSearch) {

        Page<Event> result = repository.findAll(eventSearch.getSpecification(), eventSearch.pageRequest());
        return mapper.toPageDto(result);
    }

}
