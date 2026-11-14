package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmployeeBankAccountTest {

  @ParameterizedTest
  @ValueSource(strings = {"12345678", "12345678901", "  12345678901  ", "12345678901234567890"})
  @DisplayName("Acepta cuentas numericas de 8 a 20 digitos")
  void shouldAcceptValidAccounts(final String value) {
    final EmployeeBankAccount account = new EmployeeBankAccount(value);
    assertEquals(value.trim(), account.value());
    assertEquals(value.trim(), account.toString());
  }

  @Test
  @DisplayName("Valida que la cuenta no sea nula")
  void shouldRejectNull() {
    assertThrows(NullPointerException.class, () -> new EmployeeBankAccount(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "1234567", "123456789012345678901", "1234-5678-90", "ABCDEFGHIJ", "+12345678"})
  @DisplayName("Rechaza cuentas con formato o longitud invalida")
  void shouldRejectInvalidAccounts(final String value) {
    assertThrows(InvalidEmployeeException.class, () -> new EmployeeBankAccount(value));
  }
}
