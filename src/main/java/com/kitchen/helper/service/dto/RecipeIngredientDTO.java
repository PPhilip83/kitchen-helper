package com.kitchen.helper.service.dto;

import com.kitchen.helper.domain.RecipeIngredient;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecipeIngredientDTO {
    private Long pkId;
    private Long ingredientPkId;
    private String ingredientName;
    private BigDecimal quantity;
    private String unit;

    public RecipeIngredientDTO(RecipeIngredient ri) {
        this.pkId = ri.getPkId();
        this.ingredientPkId = ri.getIngredient().getPkId();
        this.ingredientName = ri.getIngredient().getName();
        this.quantity = ri.getQuantity();
        this.unit = ri.getUnit();
    }
}
