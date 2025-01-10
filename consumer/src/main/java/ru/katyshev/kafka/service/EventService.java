package ru.katyshev.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.katyshev.kafka.model.Event;
import ru.katyshev.kafka.repository.EventRepository;

@Service
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public void save(Event event) {
        eventRepository.save(event);
    }

}
