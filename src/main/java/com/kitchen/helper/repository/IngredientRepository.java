package com.kitchen.helper.repository;

import com.kitchen.helper.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
  @Query("select i from Ingredient i where lower(i.name) like lower(concat('%', ?1, '%')) order by i.name")
  List<Ingredient> searchByName(String q);

  Optional<Ingredient> findByNameIgnoreCase(String name);
}
