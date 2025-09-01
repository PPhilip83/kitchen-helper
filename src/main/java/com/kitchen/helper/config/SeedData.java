package com.kitchen.helper.config;

import com.kitchen.helper.domain.*;
import com.kitchen.helper.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SeedData {

  private final IngredientRepository ingredientRepo;
  private final RecipeRepository recipeRepo;
  private final RecipeIngredientRepository riRepo;

  @Bean
  CommandLineRunner seedOnBoot() {
    return args -> seed();
  }

  @Transactional
  public void seed() {
    if (recipeRepo.count() > 0) {
      log.info("Seed skipped (recipes already present).");
      return;
    }

    String[] names = {
      "butter","sugar","salt","pepper","onion","garlic","bell pepper","cheddar","spaghetti noodles",
      "soy sauce","cheddar cheese","chicken breast","ground beef","italian sausage","asparagus",
      "potatoes","marinara","parmesan cheese","hot sauce","flour","yeast","carrot","lettuce","tomato",
      "white vinegar","brown sugar","bread crumbs","thyme","rosemary","sage","coriander","garam masala",
      "coconut milk","tomato paste","milk","cream","tortillas"
    };

    Map<String, Ingredient> byName = new HashMap<>();
    for (String n : names) {
      Ingredient ing = ingredientRepo.findByNameIgnoreCase(n)
          .orElseGet(() -> ingredientRepo.save(Ingredient.builder().name(n).have(true).build()));
      byName.put(n.toLowerCase(Locale.ROOT), ing);
    }

    // 2) Recipes
    Recipe butterChicken = upsertRecipe("Butter Chicken",
      "Sauté onion + garlic in butter; add garam masala, coriander, tomato paste; " +
      "stir in cream; simmer; add diced chicken and cook until done. Season with salt/pepper.");
    add(riRepo, butterChicken, byName, "butter", 3, "tbsp");
    add(riRepo, butterChicken, byName, "onion", 1, "pc");
    add(riRepo, butterChicken, byName, "garlic", 3, "clove");
    add(riRepo, butterChicken, byName, "garam masala", 2, "tsp");
    add(riRepo, butterChicken, byName, "coriander", 1, "tsp");
    add(riRepo, butterChicken, byName, "tomato paste", 2, "tbsp");
    add(riRepo, butterChicken, byName, "cream", 0.75, "cup");
    add(riRepo, butterChicken, byName, "chicken breast", 1.0, "lb");
    add(riRepo, butterChicken, byName, "salt", 0.5, "tsp");
    add(riRepo, butterChicken, byName, "pepper", 0.25, "tsp");

    Recipe spaghetti = upsertRecipe("Spaghetti and Meatballs",
      "Mix ground beef + Italian sausage with onion, garlic, bread crumbs, salt, pepper; form meatballs and brown. " +
      "Simmer in marinara; boil spaghetti; serve with parmesan.");
    add(riRepo, spaghetti, byName, "ground beef", 1.0, "lb");
    add(riRepo, spaghetti, byName, "italian sausage", 0.5, "lb");
    add(riRepo, spaghetti, byName, "onion", 0.5, "pc");
    add(riRepo, spaghetti, byName, "garlic", 2, "clove");
    add(riRepo, spaghetti, byName, "bread crumbs", 0.5, "cup");
    add(riRepo, spaghetti, byName, "salt", 0.5, "tsp");
    add(riRepo, spaghetti, byName, "pepper", 0.25, "tsp");
    add(riRepo, spaghetti, byName, "marinara", 24, "oz");
    add(riRepo, spaghetti, byName, "spaghetti noodles", 12, "oz");
    add(riRepo, spaghetti, byName, "parmesan cheese", 0.5, "cup");

    Recipe quesadillas = upsertRecipe("Chicken Quesadillas",
      "Sauté onion + bell pepper; add cooked shredded chicken and a little hot sauce. " +
      "Layer tortillas with cheddar, filling, more cheddar; toast until melty and crisp.");
    add(riRepo, quesadillas, byName, "tortillas", 4, "pc");
    add(riRepo, quesadillas, byName, "chicken breast", 0.75, "lb");
    add(riRepo, quesadillas, byName, "onion", 0.5, "pc");
    add(riRepo, quesadillas, byName, "bell pepper", 1, "pc");
    add(riRepo, quesadillas, byName, "cheddar cheese", 1.5, "cup");
    add(riRepo, quesadillas, byName, "hot sauce", 1, "tbsp");

    log.info("Seeded ingredients: {} / recipes: {}", ingredientRepo.count(), recipeRepo.count());
  }

  private Recipe upsertRecipe(String name, String instructions) {
    return recipeRepo.findByNameIgnoreCase(name)
        .orElseGet(() -> recipeRepo.save(
            Recipe.builder().name(name).instructions(instructions).build()
        ));
  }

  private static void add(RecipeIngredientRepository riRepo,
                          Recipe recipe,
                          Map<String, Ingredient> byName,
                          String ingredientName,
                          double quantity,
                          String unit) {
    Ingredient ing = byName.get(ingredientName.toLowerCase(Locale.ROOT));
    if (ing == null) return;
    riRepo.save(RecipeIngredient.builder()
        .recipe(recipe)
        .ingredient(ing)
        .quantity(BigDecimal.valueOf(quantity))
        .unit(unit)
        .build());
  }
}
