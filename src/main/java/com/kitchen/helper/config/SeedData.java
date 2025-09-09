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
  CommandLineRunner seedOnBoot() { return args -> seed(); }

  @Transactional
  public void seed() {
    Map<String, Boolean> haveMap = new LinkedHashMap<>();

    for (String n : List.of(
        "sugar","salt","pepper","onion","garlic","bell pepper","cheddar","spaghetti noodles",
        "soy sauce","cheddar cheese","chicken breast","ground beef","italian sausage","asparagus",
        "potatoes","marinara","parmesan cheese","hot sauce","flour","yeast","carrot","lettuce",
        "tomato","white vinegar","brown sugar","bread crumbs","thyme","rosemary","sage","coriander",
        "garam masala","coconut milk","tomato paste","milk","cream","tortillas","peas","ham","green beans"
    )) haveMap.put(n, true);

    for (String n : List.of(
        "butter",
        "bananas","eggs","baking soda",
        "flank steak","sesame oil","ginger","green onion","sesame seeds","gochujang","vegetable oil"
    )) haveMap.put(n, false);

    Map<String, Ingredient> byName = new HashMap<>();
    haveMap.forEach((n, have) -> {
      Ingredient ing = ingredientRepo.findByNameIgnoreCase(n)
          .orElseGet(() -> Ingredient.builder().name(n).build());
      ing.setHave(have);
      byName.put(n.toLowerCase(Locale.ROOT), ingredientRepo.save(ing));
    });

    Recipe butterChicken = upsertRecipe("Butter Chicken",
      "Sauté onion + garlic in butter; add garam masala, coriander, tomato paste; stir in cream; simmer; add diced chicken and cook until done. Season with salt/pepper.");
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
      "Mix ground beef + Italian sausage with onion, garlic, bread crumbs, salt, pepper; form meatballs and brown. Simmer in marinara; boil spaghetti; serve with parmesan.");
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
      "Sauté onion + bell pepper; add cooked shredded chicken and a little hot sauce. Layer tortillas with cheddar, filling, more cheddar; toast until melty and crisp.");
    add(riRepo, quesadillas, byName, "tortillas", 4, "pc");
    add(riRepo, quesadillas, byName, "chicken breast", 0.75, "lb");
    add(riRepo, quesadillas, byName, "onion", 0.5, "pc");
    add(riRepo, quesadillas, byName, "bell pepper", 1, "pc");
    add(riRepo, quesadillas, byName, "cheddar cheese", 1.5, "cup");
    add(riRepo, quesadillas, byName, "hot sauce", 1, "tbsp");

    Recipe bananaBread = upsertRecipe("Banana Banana Bread",
      "Whisk dry (flour, baking soda, salt). Cream butter + brown sugar; beat in eggs and mashed bananas. Combine wet + dry. Bake until a tester comes out clean.");
    add(riRepo, bananaBread, byName, "flour", 2, "cup");
    add(riRepo, bananaBread, byName, "baking soda", 1, "tsp");
    add(riRepo, bananaBread, byName, "salt", 0.25, "tsp");
    add(riRepo, bananaBread, byName, "butter", 0.5, "cup");
    add(riRepo, bananaBread, byName, "brown sugar", 0.75, "cup");
    add(riRepo, bananaBread, byName, "eggs", 2, "pc");
    add(riRepo, bananaBread, byName, "bananas", 3, "pc");

    Recipe bulgogi = upsertRecipe("Korean Beef Bulgogi",
      "Marinate thinly sliced steak in soy, brown sugar, sesame oil, garlic, ginger, gochujang. Sear in oil; finish with green onion and sesame seeds.");
    add(riRepo, bulgogi, byName, "flank steak", 1.5, "lb");
    add(riRepo, bulgogi, byName, "soy sauce", 0.33, "cup");
    add(riRepo, bulgogi, byName, "brown sugar", 3, "tbsp");
    add(riRepo, bulgogi, byName, "sesame oil", 3, "tbsp");
    add(riRepo, bulgogi, byName, "gochujang", 1, "tbsp");
    add(riRepo, bulgogi, byName, "garlic", 3, "clove");
    add(riRepo, bulgogi, byName, "ginger", 1, "tbsp");
    add(riRepo, bulgogi, byName, "green onion", 2, "pc");
    add(riRepo, bulgogi, byName, "sesame seeds", 2, "tsp");
    add(riRepo, bulgogi, byName, "vegetable oil", 2, "tbsp");

    log.info("Seed complete. Ingredients: {} | Recipes: {}", ingredientRepo.count(), recipeRepo.count());
  }

  private Recipe upsertRecipe(String name, String instructions) {
    return recipeRepo.findByNameIgnoreCase(name)
      .orElseGet(() -> recipeRepo.save(Recipe.builder().name(name).instructions(instructions).build()));
  }

  private static void add(RecipeIngredientRepository riRepo,
                          Recipe recipe,
                          Map<String, Ingredient> byName,
                          String ingredientName,
                          double quantity,
                          String unit) {
    Ingredient ing = byName.get(ingredientName.toLowerCase(Locale.ROOT));
    if (ing == null) return;
    if (!riRepo.existsByRecipePkIdAndIngredientPkId(recipe.getPkId(), ing.getPkId())) {
      riRepo.save(RecipeIngredient.builder()
          .recipe(recipe)
          .ingredient(ing)
          .quantity(BigDecimal.valueOf(quantity))
          .unit(unit)
          .build());
    }
  }
}
