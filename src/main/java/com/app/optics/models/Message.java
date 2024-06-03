package com.app.optics.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "my_message")
@Data
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue
    private Integer id;

    private String serviceId;

    private String text;
    private LocalDate date;

    private Integer customerId;

    public Message(String serviceId, String text, LocalDate date, Integer customerId) {
        this.serviceId = serviceId;
        this.text = text;
        this.date = date;
        this.customerId = customerId;
    }

    public Message(String text, LocalDate date, Integer customerId) {
        this.text = text;
        this.date = date;
        this.customerId = customerId;
    }
}
