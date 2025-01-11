package ru.katyshev.kafka.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "chatid")
    private long chatId;

    @Column(name = "event")
    private String event;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "duration")
    private long duration;

    public Event() {
    }

    @Override
    public String toString() {
        return "Event{" +
                "chatId=" + chatId +
                ", event='" + event + '\'' +
                ", createTime=" + createTime +
                ", duration=" + duration +
                '}';
    }
}
