package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmployeeLastNameTest {

  @ParameterizedTest
  @ValueSource(strings = {"Laura", "  Laura  ", "Laura\t"})
  @DisplayName("Normaliza el apellido eliminando espacios")
  void shouldNormalizeValue(final String value) {
    // Act
    final EmployeeLastName vo = new EmployeeLastName(value);
    // Assert
    assertEquals("Laura", vo.value());
    assertEquals("Laura", vo.toString());
  }

  @Test
  @DisplayName("Valida que el apellido no sea nulo")
  void shouldRejectNull() {
    assertThrows(NullPointerException.class, () -> new EmployeeLastName(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "A", " A ", "\t"})
  @DisplayName("Valida que el apellido tenga al menos 2 caracteres")
  void shouldRejectTooShortValue(final String value) {
    assertThrows(InvalidEmployeeException.class, () -> new EmployeeLastName(value));
  }
}
