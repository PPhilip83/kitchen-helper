package com.kitchen.helper.service.dto;

import java.util.List;
import lombok.*;

@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeSuggestionDTO {
  private Long recipeId;
  private String recipeName;
  private long totalCount;
  private long missingCount;
  private List<String> missingIngredientNames;
}
