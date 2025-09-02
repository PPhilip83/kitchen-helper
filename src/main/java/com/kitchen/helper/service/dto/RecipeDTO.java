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

    public RecipeDTO(Recipe r) {
        this.pkId = r.getPkId();
        this.name = r.getName();
        this.instructions = r.getInstructions();
        this.notes = r.getNotes();
        this.ingredients = r.getIngredients().stream()
            .map(RecipeIngredientDTO::new)
            .toList();
    }
}
