package com.kitchen.helper.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kitchen.helper.repository.RecipeIngredientRepository;
import com.kitchen.helper.repository.RecipeRepository;
import com.kitchen.helper.service.dto.RecipeDTO;
import com.kitchen.helper.service.dto.RecipeDetailDTO;
import com.kitchen.helper.service.dto.RecipeSuggestionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public List<RecipeDTO> findAll() {
        log.info("Fetching all recipes");
        try {
            return recipeRepository.findAllWithIngredients()
                .stream()
                .map(RecipeDTO::new)
                .toList();
        } catch (Exception e) {
            log.error("Failed to fetch recipes", e);
            throw new RuntimeException("Unable to fetch recipes at this time", e);
        }
    }

    public List<RecipeSuggestionDTO> findSuggestions(long maxMissing) {
        log.info("Finding recipe suggestions with maxMissing={}", maxMissing);
        var rows = recipeRepository.findSuggestions(maxMissing);

        return rows.stream().map(row -> {
            var missingNames = recipeIngredientRepository.findMissingNames(row.getRecipeId());
            return RecipeSuggestionDTO.builder()
                .recipeId(row.getRecipeId())
                .recipeName(row.getRecipeName())
                .totalCount(row.getTotalCount() != null ? row.getTotalCount() : 0L)
                .missingCount(row.getMissingCount() != null ? row.getMissingCount() : 0L)
                .missingIngredientNames(missingNames)
                .build();
        }).toList();
    }

    @Transactional(readOnly = true)
    public RecipeDetailDTO getDetail(Long id) {
    var recipe = recipeRepository.findByPkId(id)
    .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND, "Recipe not found: " + id));

    var rows = recipeIngredientRepository.findRowsForDetail(id);
    var ing = rows.stream().map(row -> {
        String name = (String) row[0];
        Number qty = (Number) row[1];
        String unit = (String) row[2];
        String amount = qty == null ? (unit == null ? "" : unit)
            : (unit == null || unit.isBlank() ? String.valueOf(qty) : (qty + " " + unit));
        return new RecipeDetailDTO.IngredientRow(name, amount.isBlank() ? null : amount);
        }).toList();

    return RecipeDetailDTO.builder()
        .pkId(recipe.getPkId())
        .name(recipe.getName())
        .instructions(recipe.getInstructions())
        .notes(recipe.getNotes())
        .ingredients(ing)
        .build();
    }

}

