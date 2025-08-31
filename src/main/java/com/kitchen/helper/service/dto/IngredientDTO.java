package com.kitchen.helper.service.dto;

import com.kitchen.helper.domain.Ingredient;

import lombok.Data;

@Data
public class IngredientDTO {
    private Long pkId;
    private String name;
    private boolean have = false;

    public IngredientDTO (Ingredient ingredient) {
        pkId = ingredient.getPkId();
        name = ingredient.getName();
        have = ingredient.isHave();
    }
}
