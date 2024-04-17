package com.app.optics.repositories.products;

import com.app.optics.models.products.Lenses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LensesRepository extends JpaRepository<Lenses, Integer> {
    List<Lenses> findByCustomerId(Integer id);

    void deleteAllByCustomerId(Integer id);
}
