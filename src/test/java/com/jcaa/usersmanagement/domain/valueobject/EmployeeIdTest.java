package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmployeeIdTest {

  @ParameterizedTest
  @ValueSource(strings = {"1047382910", "  1047382910  ", "1047382910\t"})
  @DisplayName("Normaliza el id del empleado eliminando espacios")
  void shouldNormalizeEmployeeId(final String value) {
    // Act
    final EmployeeId employeeId = new EmployeeId(value);
    // Assert
    assertEquals("1047382910", employeeId.value());
    assertEquals("1047382910", employeeId.toString());
  }

  @Test
  @DisplayName("Valida que el id no sea nulo")
  void shouldRejectNullId() {
    assertThrows(NullPointerException.class, () -> new EmployeeId(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  @DisplayName("Valida que el id no sea vacio")
  void shouldRejectBlankId(final String value) {
    assertThrows(InvalidEmployeeException.class, () -> new EmployeeId(value));
  }
}
