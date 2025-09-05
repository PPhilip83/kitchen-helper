package com.kitchen.helper.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kitchen.helper.domain.Ingredient;
import com.kitchen.helper.repository.IngredientRepository;
import com.kitchen.helper.service.dto.IngredientDTO;

import jakarta.persistence.EntityNotFoundException;
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

    public IngredientDTO addIngredient(String name, boolean have) {
    log.info("Adding new ingredient: {}", name);
    ingredientRepository.findByNameIgnoreCase(name).ifPresent(i -> {
        throw new IllegalStateException("Ingredient already exists: " + name);
    });
    Ingredient saved = ingredientRepository.save(
        Ingredient.builder().name(name).have(have).build()
    );
    return new IngredientDTO(saved);
    }

    public List<IngredientDTO> findAll(String query) {
        List<Ingredient> list = (query == null || query.isBlank())
            ? ingredientRepository.findAll(Sort.by(Sort.Order.asc("name").ignoreCase()))
            : ingredientRepository.findByNameContainingIgnoreCaseOrderByNameAsc(query.trim());
        return list.stream().map(IngredientDTO::new).toList();
    }

    @Transactional
    public IngredientDTO updateHave(Long id, boolean have) {
        Ingredient i = ingredientRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Ingredient not found: " + id));
        i.setHave(have);
        return new IngredientDTO(i);
    }

    @Transactional
    public void delete(Long id) {
        ingredientRepository.deleteById(id);
    }

}
