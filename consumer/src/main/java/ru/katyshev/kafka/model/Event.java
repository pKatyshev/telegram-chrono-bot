package ru.katyshev.kafka.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    public Event() {
    }

    @Override
    public String toString() {
        return "Event{" +
                "chatId=" + chatId +
                ", event='" + event + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
