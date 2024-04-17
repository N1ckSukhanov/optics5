package com.app.optics.repositories.products;

import com.app.optics.models.products.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByCustomerId(Integer id);

    void deleteAllByCustomerId(Integer id);

}
