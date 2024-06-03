package com.app.optics.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_example")
@Data
@NoArgsConstructor
public class MessageExample {
    @Id
    @GeneratedValue
    private Integer id;

    private String lenses;
    private String recipe;

    public MessageExample(String lenses, String recipe) {
        this.lenses = lenses;
        this.recipe = recipe;
    }
}
