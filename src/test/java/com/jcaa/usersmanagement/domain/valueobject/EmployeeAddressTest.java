package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmployeeAddressTest {

  @Test
  @DisplayName("Normaliza la direccion eliminando espacios")
  void shouldNormalizeAddress() {
    final EmployeeAddress address = new EmployeeAddress("  Calle 10 # 5-20  ");
    assertEquals("Calle 10 # 5-20", address.value());
    assertEquals("Calle 10 # 5-20", address.toString());
  }

  @Test
  @DisplayName("Valida que la direccion no sea nula")
  void shouldRejectNull() {
    assertThrows(NullPointerException.class, () -> new EmployeeAddress(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "\t"})
  @DisplayName("Valida que la direccion no sea vacia")
  void shouldRejectBlank(final String value) {
    assertThrows(InvalidEmployeeException.class, () -> new EmployeeAddress(value));
  }
}
