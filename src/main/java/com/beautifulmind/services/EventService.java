package com.beautifulmind.services;

import com.beautifulmind.model.Event;
import com.beautifulmind.repositories.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Optional<Event> getEventById(long id) {
        log.info("Searching for the event (ID: {})", id);
        return eventRepository.findById(id);
    }

    public Iterable<Event> getAllEventsByDate(LocalDateTime localDateTime) {
        log.info("Searching for all the events on {}", localDateTime);
        return eventRepository.findAllByDate(localDateTime);
    }

}
