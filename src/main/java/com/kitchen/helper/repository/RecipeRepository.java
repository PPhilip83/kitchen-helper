package com.kitchen.helper.repository;

import com.kitchen.helper.domain.Recipe;
import com.kitchen.helper.service.projection.RecipeSuggestion;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByNameIgnoreCase(String name);

    @Query("select r.pkId as recipeId, r.name as recipeName, count(ri) as totalCount, sum(case when i.have = false then 1L else 0L end) as missingCount from RecipeIngredient ri join ri.recipe r join ri.ingredient i group by r.pkId, r.name having sum(case when i.have = false then 1L else 0L end) <= :maxMissing order by sum(case when i.have = false then 1L else 0L end), r.name")
    List<RecipeSuggestion> findSuggestions(long maxMissing);

    @EntityGraph(attributePaths = {"ingredients", "ingredients.ingredient"})
    @Query("select r from Recipe r")
    List<Recipe> findAllWithIngredients();

    @Query("select r from Recipe r where r.pkId = :id")
    Optional<Recipe> findByPkId(Long id);
    
}
