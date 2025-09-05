package com.kitchen.helper.web;

import com.kitchen.helper.service.IngredientService;
import com.kitchen.helper.service.dto.CreateIngredientRequest;
import com.kitchen.helper.service.dto.IngredientDTO;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ingredients")
@AllArgsConstructor
public class IngredientResource {

private static final Logger log = LoggerFactory.getLogger(IngredientResource.class);

  private final IngredientService ingredientService;

  @PostMapping
  public ResponseEntity<IngredientDTO> addIngredient(@RequestBody @Valid CreateIngredientRequest body) {
    log.info("Request to add new ingredient {}", body.name());
    IngredientDTO created = ingredientService.addIngredient(body.name(), body.have());
    URI location = URI.create("/api/ingredients/" + created.getPkId());
    return ResponseEntity.created(location).body(created);
  }

  @GetMapping
  public List<IngredientDTO> list(@RequestParam(value = "query", required = false) String query) {
    return ingredientService.findAll(query);
  }

    @PatchMapping("/{id}/have")
    public IngredientDTO toggleHave(@PathVariable Long id, @RequestParam("value") boolean have) {
      return ingredientService.updateHave(id, have);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
      ingredientService.delete(id);
      return ResponseEntity.noContent().build();
    }
}
