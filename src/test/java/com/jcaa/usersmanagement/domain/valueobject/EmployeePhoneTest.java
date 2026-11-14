package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmployeePhoneTest {

  @ParameterizedTest
  @ValueSource(strings = {"3001234567", "+573001234567", "  3001234567  ", "1234567"})
  @DisplayName("Acepta telefonos numericos de 7 a 15 digitos con '+' opcional")
  void shouldAcceptValidPhones(final String value) {
    // Act
    final EmployeePhone phone = new EmployeePhone(value);
    // Assert
    assertEquals(value.trim(), phone.value());
    assertEquals(value.trim(), phone.toString());
  }

  @Test
  @DisplayName("Valida que el telefono no sea nulo")
  void shouldRejectNull() {
    assertThrows(NullPointerException.class, () -> new EmployeePhone(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "123456", "1234567890123456", "300-123-4567", "abcdefghij", "++5730012345"})
  @DisplayName("Rechaza telefonos con formato o longitud invalida")
  void shouldRejectInvalidPhones(final String value) {
    assertThrows(InvalidEmployeeException.class, () -> new EmployeePhone(value));
  }
}
