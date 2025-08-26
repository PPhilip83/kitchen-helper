package com.kitchen.helper.web;

import com.kitchen.helper.domain.Ingredient;
import com.kitchen.helper.repository.IngredientRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientResource {
  private final IngredientRepository repo;
  public IngredientResource(IngredientRepository repo) { this.repo = repo; }
  @GetMapping public List<Ingredient> all() { return repo.findAll(); }
}
