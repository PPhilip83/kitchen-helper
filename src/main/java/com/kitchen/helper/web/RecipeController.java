package com.kitchen.helper.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kitchen.helper.service.RecipeService;
import com.kitchen.helper.service.dto.RecipeDTO;
import com.kitchen.helper.service.dto.RecipeSuggestionDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
        log.info("Request to get all recipes");
        List<RecipeDTO> recipes = recipeService.findAll();
        if (recipes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<RecipeSuggestionDTO>> suggestions(
        @RequestParam(defaultValue = "0") long maxMissing) {
    var list = recipeService.findSuggestions(maxMissing);
    return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

}
