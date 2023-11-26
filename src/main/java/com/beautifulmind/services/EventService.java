package com.beautifulmind.services;

import com.beautifulmind.model.Event;
import com.beautifulmind.model.EventSnapshotDTO;
import com.beautifulmind.repositories.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public Map<LocalDate, List<Event>> getAllEventsByMonth(LocalDate localDate) {
        var events = eventRepository.findAllByMonth(localDate);
        return StreamSupport.stream(events.spliterator(), false)
                .collect(Collectors.groupingBy(Event::getDateId));
    }

    public Map<LocalDate, List<EventSnapshotDTO>> getAllEventSnapshotsByMonth(LocalDate localDate) {
        var eventSnapshots = eventRepository.findAllEventSnapshotsByMonth(localDate);
        return StreamSupport.stream(eventSnapshots.spliterator(), false)
                .collect(Collectors.groupingBy(EventSnapshotDTO::getDate));
    }

    public Iterable<Event> getAllEventsByDate(LocalDateTime localDateTime) {
        log.info("Searching for all the events on {}", localDateTime);
        return eventRepository.findAllByDate(localDateTime);
    }

    public Event createEvent(Event event) {
        var dateId = event.getStartDateTime().toLocalDate();
        event.setDateId(dateId);
        return eventRepository.saveEvent(event);
    }

    public void deleteEvent(int eventId) {
        eventRepository.deleteEvent(eventId);
    }

}
