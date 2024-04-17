package com.app.optics.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static com.app.optics.util.ObjectConverter.readableDate;
import static com.app.optics.util.ObjectConverter.str;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", allocationSize = 1)
    private Integer id;

    private String name;

    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Transient
    private Integer productCount;
    @Transient
    private Integer productSum;
    @Transient
    private Integer discount;

    @Override
    public String toString() {
        return str("Имя", name) +
                str("Телефон", phone) +
                str("Дата рождения", readableDate(birthday));
    }
}
