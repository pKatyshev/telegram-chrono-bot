package ru.katyshev.kafka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.katyshev.kafka.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
}
