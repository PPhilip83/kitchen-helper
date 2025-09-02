package com.kitchen.helper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.data.repository.cdi.Eager;

@Entity
@Table(name = "recipe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Recipe {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long pkId;

  @NotBlank
  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "text")
  private String instructions;

  @Column(columnDefinition = "text")
  private String notes;

  @Builder.Default
  @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<RecipeIngredient> ingredients = new LinkedHashSet<>();


  public void addItem(RecipeIngredient ri) { ri.setRecipe(this); ingredients.add(ri); }
  public void removeItem(RecipeIngredient ri) { ingredients.remove(ri); ri.setRecipe(null); }
}
