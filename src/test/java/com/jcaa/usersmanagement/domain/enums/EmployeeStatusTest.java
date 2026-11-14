package com.jcaa.usersmanagement.domain.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmployeeStatusTest {

  @ParameterizedTest
  @ValueSource(strings = {"ACTIVE", "active", "Active"})
  @DisplayName("fromString() resuelve ACTIVE sin importar mayusculas")
  void shouldResolveActive(final String value) {
    assertEquals(EmployeeStatus.ACTIVE, EmployeeStatus.fromString(value));
  }

  @ParameterizedTest
  @ValueSource(strings = {"INACTIVE", "inactive"})
  @DisplayName("fromString() resuelve INACTIVE sin importar mayusculas")
  void shouldResolveInactive(final String value) {
    assertEquals(EmployeeStatus.INACTIVE, EmployeeStatus.fromString(value));
  }

  @Test
  @DisplayName("fromString() lanza InvalidEmployeeException con un valor desconocido")
  void shouldRejectUnknownStatus() {
    assertThrows(InvalidEmployeeException.class, () -> EmployeeStatus.fromString("BLOCKED"));
  }
}
