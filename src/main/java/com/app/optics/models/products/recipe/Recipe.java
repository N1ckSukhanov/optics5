package com.app.optics.models.products.recipe;

import com.app.optics.models.products.Product;
import com.app.optics.models.products.ProductType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static com.app.optics.util.ObjectConverter.readableDate;
import static com.app.optics.util.ObjectConverter.str;

@Entity
@Table(name = "recipe")
@Data
@EqualsAndHashCode(callSuper = true)
public class Recipe extends Product {
    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Cost cost;

    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Oculus oculus;

    @Column(name = "order_id")
    protected Integer orderId;

    @Column(name = "for_who")
    private String forWho;

    private String frame;

    private String ratio;

    private String coverage;

    private String geometry;

    private Integer distance;

    @Column(name = "from_who")
    private String fromWho;

    private String info;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;

    @Transient
    protected String customerName;
    @Transient
    protected String customerPhone;

    public Recipe() {
        super(ProductType.RECIPE);
        oculus = new Oculus();
        oculus.setRecipe(this);
        cost = new Cost();
        cost.setRecipe(this);
    }

    public void setDiscountPercent(int discountPercent){
        cost.setDiscountPercent(discountPercent);
    }

    public Recipe(ProductType type, Integer orderId, String forWho, String frame, String ratio, String coverage, String geometry, Integer distance, String info, LocalDate deliveryDate) {
        this.productType = type;
        this.orderId = orderId;
        this.forWho = forWho;
        this.frame = frame;
        this.ratio = ratio;
        this.coverage = coverage;
        this.geometry = geometry;
        this.distance = distance;
        this.info = info;
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return str("Номер заказа", orderId) +
                str("Для кого", forWho) +
                str("Оправа", frame) +
                str("Коэфф.", ratio) +
                str("Покрытие", coverage) +
                str("Геометрия", geometry) +
                oculus +
                str("РЦ", distance) +
                str("Рецепт", fromWho) +
                str("Примечания", info) +
                str("Дата покупки", readableDate(receptionDate)) +
                str("Дата выдачи", readableDate(deliveryDate)) +
                cost;
    }

    @Override
    public String productHeader() {
        return "Заказ на Очки";
    }

    @Override
    public String shortName() {
        String p = (cost != null && cost.getTotal() != null) ? String.valueOf(cost.getTotal()) : "0";
        return productHeader() + " : " + readableDate(receptionDate) + " : " + p + "₽";
    }

    public void setDiscountFromOthers(){
        if (cost == null || cost.getTotal() == null || cost.getSum() == null)
            return;
        cost.setDiscount(cost.getSum() - cost.getTotal());
    }
}


