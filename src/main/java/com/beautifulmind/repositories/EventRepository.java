package com.beautifulmind.repositories;

import com.beautifulmind.model.Event;
import com.beautifulmind.model.EventSnapshotDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface EventRepository {
    Optional<Event> findById(long id);

    Iterable<Event> findAllByDate(LocalDateTime dateTime);

    Iterable<Event> findAllByMonth(LocalDate date);

    Iterable<EventSnapshotDTO> findAllEventSnapshotsByMonth(LocalDate date);

    Event saveEvent(Event event);

    void deleteEvent(int eventId);

    Event updateEvent(Event event);
}
