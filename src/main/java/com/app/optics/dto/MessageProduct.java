package com.app.optics.dto;

import com.app.optics.models.Message;
import com.app.optics.models.products.Lenses;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MessageProduct {
    private Integer id;
    private String phone;
    private String name;
    private LocalDate date;
    private String text;
}
