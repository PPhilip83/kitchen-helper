package com.kitchen.helper.web;

import com.kitchen.helper.domain.Ingredient;
import com.kitchen.helper.service.IngredientService;
import com.kitchen.helper.service.dto.IngredientDTO;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ingredients")
@AllArgsConstructor
public class IngredientResource {

private static final Logger log = LoggerFactory.getLogger(IngredientResource.class);

  private final IngredientService ingredientService;

  @GetMapping 
  public List<IngredientDTO> findAll() {
    log.info("Request to get all ingredients");
    return ingredientService.findAll(); 
  }

  @PostMapping
  public IngredientDTO addIngreditnt(Ingredient ingredient) {
    log.info("Request to add new ingredient {} ", ingredient.getName());
    return ingredientService.addIngredient(ingredient);
  }
}
