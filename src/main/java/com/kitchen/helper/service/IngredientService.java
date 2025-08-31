package com.kitchen.helper.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kitchen.helper.domain.Ingredient;
import com.kitchen.helper.repository.IngredientRepository;
import com.kitchen.helper.service.dto.IngredientDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class IngredientService {

    private static final Logger log = LoggerFactory.getLogger(IngredientService.class);

    private final IngredientRepository ingredientRepository;

    public List<IngredientDTO> findAll() {
        log.info("Fetching all ingredients");
        try {
            return ingredientRepository.findAll()
                .stream()
                .map(IngredientDTO::new)
                .toList();
        } catch (Exception e) {
            log.error("Failed to fetch ingredients", e);
            throw new RuntimeException("Unable to fetch ingredients at this time", e);
        }
    }

    public IngredientDTO addIngredient(Ingredient ingredient) {
        log.info("Adding new ingredient: {}", ingredient.getName());
        try {
            Ingredient newIngredient = new Ingredient();
            newIngredient.setName(ingredient.getName());
            newIngredient.setHave(ingredient.isHave());

            Ingredient saved = ingredientRepository.save(newIngredient);
            return new IngredientDTO(saved);
        } catch (Exception e) {
            log.error("Failed to add ingredient {}", ingredient.getName(), e);
            throw new RuntimeException("Unable to add ingredient at this time", e);
        }
    }
}
