package com.app.optics.repositories;

import com.app.optics.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findAllByNameOrPhone(String name, String phone);
}
