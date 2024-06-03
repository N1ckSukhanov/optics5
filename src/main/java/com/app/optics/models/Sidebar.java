package com.app.optics.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sidebar")
@Data
@NoArgsConstructor
public class Sidebar {
    @Id
    @GeneratedValue
    private Integer id;

    private boolean product = false;
    private boolean old = false;
    private boolean message = false;
    private boolean other = false;
}
