package ru.katyshev.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.katyshev.kafka.dto.EventDTO;
import ru.katyshev.kafka.model.Event;
import ru.katyshev.kafka.service.EventService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("event")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("test")
    public void testJson(@RequestBody EventDTO dto) {
        Event e = new Event();
        e.setChatId(dto.getChatId());
        e.setEvent(dto.getEvent());
        e.setCreateTime(LocalDateTime.now());

        eventService.save(e);
    }
}
