package com.kitchen.helper.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kitchen.helper.repository.RecipeRepository;
import com.kitchen.helper.service.dto.RecipeDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {

    private final RecipeRepository recipeRepository;

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
}

