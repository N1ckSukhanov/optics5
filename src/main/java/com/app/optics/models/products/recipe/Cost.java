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
//    @SequenceGenerator(name = "cost_seq", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cost_seq")
    private Integer id;
    //    @OneToOne(mappedBy = "cost")
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

    public Cost(Integer frame, Integer lenses, Integer work, Integer sum, Integer discount, Integer total, Integer paid, Integer extra) {
        this.frame = frame;
        this.lenses = lenses;
        this.work = work;
        this.sum = sum;
        this.discount = discount;
        this.total = total;
        this.paid = paid;
        this.extra = extra;
    }

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
