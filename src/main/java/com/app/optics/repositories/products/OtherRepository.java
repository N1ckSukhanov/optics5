package com.app.optics.repositories.products;

import com.app.optics.models.products.Other;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtherRepository extends JpaRepository<Other, Integer> {
    List<Other> findByCustomerId(Integer id);

    void deleteAllByCustomerId(Integer id);
}
