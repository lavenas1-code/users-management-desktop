package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import java.util.Objects;

public record EmployeeAddress(String value) {

  public EmployeeAddress {
    final String normalizedValue =
        Objects.requireNonNull(value, "EmployeeAddress no puede ser nulo").trim();
    validateNotEmpty(normalizedValue);
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidEmployeeException.becauseAddressIsEmpty();
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
