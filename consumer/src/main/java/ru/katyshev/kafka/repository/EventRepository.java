package ru.katyshev.kafka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.katyshev.kafka.model.Event;
import ru.katyshev.kafka.model.EventSum;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByChatId(long chatId);

    @Query(value = "SELECT MAX(create_time) FROM event WHERE chatid = ?1", nativeQuery = true )
    LocalDateTime findLastCreateTimeByChatId(long chatId);

    @Query(value = "SELECT * FROM event WHERE create_time = (SELECT MAX(create_time) FROM event WHERE chatid = ?1)", nativeQuery = true)
    Event findLastEventByChatId(long chatId);

    void deleteAllByChatId(long chatId);

    @Query("SELECT new ru.katyshev.kafka.model.EventSum(ev.event, SUM(ev.duration)) from Event AS ev WHERE ev.chatId = ?1 group by ev.event")
    List<EventSum> getEventSummaryList(long chatId);
}
