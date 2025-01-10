package ru.katyshev.kafka.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.katyshev.kafka.dto.EventDTO;
import ru.katyshev.kafka.model.Event;
import ru.katyshev.kafka.service.EventService;

import java.time.LocalDateTime;

@Component
@EnableKafka
public class KafkaEventListener {
    private final EventService eventService;

    @Autowired
    public KafkaEventListener(EventService eventService) {
        this.eventService = eventService;
    }

    @KafkaListener(id = "app.1", topics = "evnt")
    public void eventListener(@Payload EventDTO dto) {
        Event e = new Event();
        e.setChatId(dto.getChatId());
        e.setEvent(dto.getEvent());
        e.setCreateTime(LocalDateTime.now());

        eventService.save(e);
    }

}
