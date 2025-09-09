package com.kitchen.helper.service.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDetailDTO {
  private Long pkId;
  private String name;
  private String instructions;
  private String notes;
  private List<IngredientRow> ingredients;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class IngredientRow {
    private String name;
    private String amount;
  }
}
