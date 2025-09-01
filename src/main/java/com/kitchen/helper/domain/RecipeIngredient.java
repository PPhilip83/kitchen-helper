package com.kitchen.helper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
  name = "recipe_ingredient",
  uniqueConstraints = @UniqueConstraint(name = "uq_recipe_ingredient", columnNames = {"recipe_id","ingredient_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecipeIngredient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long pkId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "recipe_id", nullable = false)
  @JsonIgnore
  private Recipe recipe;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ingredient_id", nullable = false)
  private Ingredient ingredient;

  @Column(precision = 10, scale = 2)
  private BigDecimal quantity;

  @Column(length = 16)
  private String unit;
}
