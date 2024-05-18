package com.app.optics.models.products.recipe;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.app.optics.util.ObjectConverter.str;

@Entity
@Table(name = "cost")
@Data
@NoArgsConstructor
public class Cost {
    @Id
    private Integer id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Recipe recipe;
    private Integer frame;
    private Integer lenses;
    private Integer work;
    private Integer sum;
    private Integer discount;
    private Integer total;
    private Integer paid;
    private Integer extra;

    private Integer discountPercent;

    @Override
    public String toString() {
        Integer d = (sum != null && total != null) ? sum - total : null;
        return "Стоимость\n" +
                str("Оправа", frame) +
                str("Линзы", lenses) +
                str("Работа", work) +
                str("Сумма", sum) +
                str("Скидка", d) +
                str("Итого", total) +
                str("Оплачено", paid) +
                str("Доплата", extra);
    }
}
