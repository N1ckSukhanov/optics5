package com.app.optics.models.products;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.app.optics.util.ObjectConverter.str;

@Entity
@Table(name = "lenses")
@Data
@EqualsAndHashCode(callSuper = true)
public class Lenses extends Product {
    @Column
    private Float power;

    private Float curvature;

    private String color;

    public Lenses() {
        super(ProductType.LENSES);
    }

    public Lenses(Float power, Float curvature, String color) {
        super(ProductType.LENSES);
        this.power = power;
        this.curvature = curvature;
        this.color = color;
    }

    @Override
    public String toString() {
        return super.toString() +
                str("Оптич. сила", power) +
                str("Кривизна", curvature) +
                str("Цвет", color);
    }

    @Override
    public String productHeader() {
        return "Контактные Линзы";
    }
}
