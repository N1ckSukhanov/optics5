package com.app.optics.models.products;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static com.app.optics.util.ObjectConverter.readableDate;
import static com.app.optics.util.ObjectConverter.str;

@Data
@NoArgsConstructor
@MappedSuperclass
public class Product implements Comparable<Product> {
    @Id
    @SequenceGenerator(name = "product_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    protected Integer id;

    @Column(name = "customer_id")
    protected Integer customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    protected ProductType productType;

    protected String name; // not Recipe

    protected Integer price; // not Recipe

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate receptionDate = LocalDate.now();

    @Transient
    protected Boolean newLine = false;

    @Transient
    protected Boolean opened = false;

    protected Integer serialNumber;

    public Product(ProductType productType) {
        this.productType = productType;
    }

    public String productHeader() {
        return "";
    }

    @Override
    public String toString() {
        return str("Название", name) +
                str("Дата покупки", receptionDate) +
                str("Цена", price);
    }

    public String shortName() {
        return productHeader() + " : " + readableDate(receptionDate) + " : " + price + "₽";
    }

    @Override
    public int compareTo(Product o) {
        if (receptionDate == null)
            return -1;
        if (o.getReceptionDate() == null)
            return 1;
        if (o.getReceptionDate().equals(receptionDate))
            return o.getId().compareTo(id);
        return o.getReceptionDate().compareTo(receptionDate);
    }
}
