package com.kitchen.helper.service;

import com.kitchen.helper.domain.Ingredient;
import com.kitchen.helper.repository.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTests {

  @Mock private IngredientRepository ingredientRepository;

  private IngredientService service;

  @BeforeEach
  void setUp() {
    service = new IngredientService(ingredientRepository);
  }

  @Test
  void findAll_returnsMappedDTOs() {
    var butter = Ingredient.builder().pkId(1L).name("Butter").have(true).build();
    var sugar  = Ingredient.builder().pkId(2L).name("Sugar").have(false).build();
    when(ingredientRepository.findAll()).thenReturn(List.of(butter, sugar));

    var result = service.findAll();

    assertEquals(2, result.size());
    assertEquals("Butter", result.get(0).getName());
    assertTrue(result.get(0).isHave());
    assertEquals("Sugar",  result.get(1).getName());
    assertFalse(result.get(1).isHave());
    verify(ingredientRepository).findAll();
    verifyNoMoreInteractions(ingredientRepository);
  }

  @Test
  void findAll_wrapsRepositoryErrors() {
    when(ingredientRepository.findAll()).thenThrow(new RuntimeException("db down"));

    var ex = assertThrows(RuntimeException.class, () -> service.findAll());
    assertTrue(ex.getMessage().contains("Unable to fetch ingredients"));
    assertNotNull(ex.getCause());
    verify(ingredientRepository).findAll();
  }

  @Test
  void addIngredient_saves_whenNotExists() {
    when(ingredientRepository.findByNameIgnoreCase("Butter")).thenReturn(Optional.empty());
    var saved = Ingredient.builder().pkId(10L).name("Butter").have(true).build();
    when(ingredientRepository.save(any(Ingredient.class))).thenReturn(saved);

    var dto = service.addIngredient("Butter", true);

    assertEquals("Butter", dto.getName());
    assertTrue(dto.isHave());
    verify(ingredientRepository).findByNameIgnoreCase("Butter");
    ArgumentCaptor<Ingredient> captor = ArgumentCaptor.forClass(Ingredient.class);
    verify(ingredientRepository).save(captor.capture());
    assertEquals("Butter", captor.getValue().getName());
    assertTrue(captor.getValue().isHave());
  }

  @Test
  void addIngredient_throws_ifDuplicateName() {
    var existing = Ingredient.builder().pkId(5L).name("Butter").have(true).build();
    when(ingredientRepository.findByNameIgnoreCase("Butter")).thenReturn(Optional.of(existing));

    var ex = assertThrows(IllegalStateException.class, () -> service.addIngredient("Butter", true));
    assertTrue(ex.getMessage().contains("already exists"));
    verify(ingredientRepository).findByNameIgnoreCase("Butter");
    verify(ingredientRepository, never()).save(any());
  }

  @Test
  void findAll_withBlankQuery_usesSortedFindAll() {
    var butter = Ingredient.builder().pkId(1L).name("Butter").have(true).build();
    when(ingredientRepository.findAll(any(Sort.class))).thenReturn(List.of(butter));

    var result = service.findAll("   ");
    assertEquals(1, result.size());
    assertEquals("Butter", result.get(0).getName());

    ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
    verify(ingredientRepository).findAll(sortCaptor.capture());
    var sort = sortCaptor.getValue();
    assertNotNull(sort);
    assertTrue(sort.stream().anyMatch(order -> order.getProperty().equals("name")));
  }

  @Test
  void findAll_withQuery_usesNameContainsIgnoreCase() {
    var butter = Ingredient.builder().pkId(1L).name("Butter").have(true).build();
    when(ingredientRepository.findByNameContainingIgnoreCaseOrderByNameAsc("but"))
        .thenReturn(List.of(butter));

    var result = service.findAll("  but  ");
    assertEquals(1, result.size());
    assertEquals("Butter", result.get(0).getName());

    verify(ingredientRepository)
        .findByNameContainingIgnoreCaseOrderByNameAsc("but");
    verify(ingredientRepository, never()).findAll(any(Sort.class));
  }

  @Test
  void updateHave_setsFlag_andReturnsDTO() {
    var sugar = Ingredient.builder().pkId(2L).name("Sugar").have(false).build();
    when(ingredientRepository.findById(2L)).thenReturn(Optional.of(sugar));

    var dto = service.updateHave(2L, true);

    assertTrue(dto.isHave());
    assertTrue(sugar.isHave(), "entity flag should be updated (dirty-check on tx)");
    verify(ingredientRepository).findById(2L);
    verify(ingredientRepository, never()).save(any());
  }

  @Test
  void updateHave_throws_ifNotFound() {
    when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> service.updateHave(99L, true));
    verify(ingredientRepository).findById(99L);
  }

  @Test
  void delete_delegatesToRepository() {
    service.delete(7L);
    verify(ingredientRepository).deleteById(7L);
    verifyNoMoreInteractions(ingredientRepository);
  }
}

