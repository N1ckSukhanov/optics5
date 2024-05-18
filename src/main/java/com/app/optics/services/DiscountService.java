package com.app.optics.services;

import com.app.optics.models.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DiscountService {
    public Integer getCustomerDiscount(Customer customer) {
        if (customer == null) {
            return 0;
        }

        LocalDate birthday = customer.getBirthday();
        if (birthday != null) {
            LocalDate now = LocalDate.now();
            if (birthday.getMonthValue() == now.getMonthValue() && birthday.getDayOfMonth() == now.getDayOfMonth()) {
                return 15;
            }
        }

        int sum = customer.getProductSum();
        Integer count = customer.getProductCount();
        if (count == null) {
            return 0;
        }
        if (count < 1)
            return 0;
        else if (count < 11)
            return count + 4;
        else
            return 15;
    }
}
