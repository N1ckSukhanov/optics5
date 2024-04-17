package com.app.optics.repositories;

import com.app.optics.models.OptionType;
import com.app.optics.models.products.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    List<Option> findAllByType(OptionType type);
}
