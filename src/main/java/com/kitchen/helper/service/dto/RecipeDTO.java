package com.kitchen.helper.service.dto;

import com.kitchen.helper.domain.Recipe;
import lombok.Data;

import java.util.List;

@Data
public class RecipeDTO {
  private Long pkId;
  private String name;
  private String instructions;
  private String notes;
  private List<RecipeIngredientDTO> ingredients;

  public RecipeDTO(Recipe recipe) {
    this.pkId = recipe.getPkId();
    this.name = recipe.getName();
    this.instructions = recipe.getInstructions();
    this.notes = recipe.getNotes();
    this.ingredients = recipe.getIngredients().stream()
        .map(RecipeIngredientDTO::new)
        .toList();
  }
}

