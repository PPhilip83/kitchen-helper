package com.kitchen.helper.service.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateIngredientRequest(@NotBlank String name, boolean have) {}

