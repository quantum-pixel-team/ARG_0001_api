package com.quantum_pixel.arg.event.web.controller;

import com.quantum_pixel.arg.event.service.EventService;
import com.quantum_pixel.arg.event.specification.EventSearch;
import com.quantum_pixel.arg.v1.web.EventApi;
import com.quantum_pixel.arg.v1.web.model.EventDTO;
import com.quantum_pixel.arg.v1.web.model.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController implements EventApi {

    private final EventService service;

    @Override
    public ResponseEntity<PageDTO> getAllUpcomingEvents(Integer pageIndex, Integer pageSize, String filter) {
        var res = service.getEvents(new EventSearch(PageRequest.of(pageIndex, pageSize), filter));
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<Void> createEvent(EventDTO eventDTO) {
        service.createEvent(eventDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
