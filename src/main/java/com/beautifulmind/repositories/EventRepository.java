package com.beautifulmind.repositories;

import com.beautifulmind.model.Event;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EventRepository {
    Optional<Event> findById(long id);
    Iterable<Event> findAllByDate(LocalDateTime dateTime);
    void saveEvent(Event event);
}
