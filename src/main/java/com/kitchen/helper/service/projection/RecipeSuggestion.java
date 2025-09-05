package com.kitchen.helper.service.projection;

public interface RecipeSuggestion {
  Long getRecipeId();
  String getRecipeName();
  Long getTotalCount();
  Long getMissingCount();
}
