package com.kitchen.helper.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kitchen.helper.repository.RecipeIngredientRepository;
import com.kitchen.helper.repository.RecipeRepository;
import com.kitchen.helper.service.dto.RecipeDTO;
import com.kitchen.helper.service.dto.RecipeSuggestionDTO;

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

}

