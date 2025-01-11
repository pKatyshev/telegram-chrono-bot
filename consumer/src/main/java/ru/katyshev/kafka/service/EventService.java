package ru.katyshev.kafka.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.katyshev.kafka.dto.ServerResponseDTO;
import ru.katyshev.kafka.model.Event;
import ru.katyshev.kafka.model.EventSum;
import ru.katyshev.kafka.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Log
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public void save(Event event) {
        setTimings(event);
        eventRepository.save(event);
    }

    public void setTimings(Event newEvent) {
        newEvent.setCreateTime(LocalDateTime.now());
        Event lastEvent = eventRepository.findLastEventByChatId(newEvent.getChatId());
        if (lastEvent == null) {
            log.info("new user");
        } else {
            long duration = ChronoUnit.SECONDS.between(lastEvent.getCreateTime(), newEvent.getCreateTime());
            lastEvent.setDuration(duration);
        }
    }

    public ServerResponseDTO getReport(long chatId) {
        List<EventSum> eventSums = eventRepository.getEventSummaryList(chatId);
        String textReport = constructTextReport(eventSums);

        return ServerResponseDTO.builder()
                .chatId(chatId)
                .response(textReport)
                .build();
    }

    @Transactional
    public ServerResponseDTO deleteUserData(long chatId) {
        eventRepository.deleteAllByChatId(chatId);

        return ServerResponseDTO.builder()
                .chatId(chatId)
                .response("Successfully deleted")
                .build();
    }

    private String constructTextReport(List<EventSum> eventSums) {
        StringBuilder sb = new StringBuilder();
        if (eventSums.size() == 0) {
            return "Нет данных";
        }
        sb.append("Отчет: ");

        for (EventSum es: eventSums) {
            if (es.getEvent().equals("Пауза")) continue;
            LocalTime time = LocalTime.ofSecondOfDay(es.getSum());
            sb.append("\n").append(time);
            sb.append(" - ");
            sb.append(es.getEvent());
        }

        return sb.toString();
    }
}
