package com.kitchen.helper.repository;

import com.kitchen.helper.domain.RecipeIngredient;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    @Query("select i.name from RecipeIngredient ri join ri.ingredient i where ri.recipe.pkId = :recipeId and i.have = false order by i.name")
    List<String> findMissingNames(Long recipeId);

    boolean existsByRecipePkIdAndIngredientPkId(Long recipePkId, Long ingredientPkId);

    @Query("select i.name, ri.quantity, ri.unit from RecipeIngredient ri join ri.ingredient i where ri.recipe.pkId = :id order by i.name")
    List<Object[]> findRowsForDetail(Long id);

}
