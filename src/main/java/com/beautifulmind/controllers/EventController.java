package com.beautifulmind.controllers;

import com.beautifulmind.model.Event;
import com.beautifulmind.services.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable long id) {
        var event = eventService.getEventById(id);

        if (event.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(event.get());
        }

        return ResponseEntity
                .badRequest()
                .body("There is no Event with ID: %d".formatted(id));
    }

    @PostMapping()
    public Event saveEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable int id) {
        eventService.deleteEvent(id);
    }

    @PatchMapping
    public Event updateEvent(@RequestBody Event event) {
        return eventService.updateEvent(event);
    }
}
