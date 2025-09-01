package com.kitchen.helper.service.dto;

import java.math.BigDecimal;

import com.kitchen.helper.domain.RecipeIngredient;
import lombok.Data;

@Data
public class RecipeIngredientDTO {
  private Long pkId;
  private Long ingredientPkId;
  private String ingredientName;
  private BigDecimal quantity;
  private String unit;

  public RecipeIngredientDTO(RecipeIngredient recipeIngredient) {
    this.pkId = recipeIngredient.getPkId();
    this.ingredientPkId = recipeIngredient.getIngredient().getPkId();
    this.ingredientName = recipeIngredient.getIngredient().getName();
    this.quantity = recipeIngredient.getQuantity();
    this.unit = recipeIngredient.getUnit();
  }
}

