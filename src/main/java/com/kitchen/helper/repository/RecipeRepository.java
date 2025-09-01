package com.kitchen.helper.repository;

import com.kitchen.helper.domain.Recipe;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = {"ingredients", "ingredients.ingredient"})
    @Query("select r from Recipe r")
    List<Recipe> findAllWithIngredients();
}
