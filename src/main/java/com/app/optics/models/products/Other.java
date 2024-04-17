package com.app.optics.models.products;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.app.optics.util.ObjectConverter.str;


@Entity
@Table(name = "other")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Other extends Product {
    @Column
    private String article;

    public Other(ProductType productType) {
        super(productType);
    }

    public String productHeader() {
        return switch (productType) {
            case SUNGLASSES -> "Солнцезащитные Очки";
            case ACCESSORY -> "Аксессуары";
            case CASE -> "Футляры";
            case OTHER -> "Прочее";
            default -> "";
        };
    }

    @Override
    public String toString() {
        String article = productType == ProductType.SUNGLASSES ? str("Артикул", this.article) : "";
        return article + super.toString();
    }
}
