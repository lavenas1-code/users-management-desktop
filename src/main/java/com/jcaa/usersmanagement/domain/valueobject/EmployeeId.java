package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import java.util.Objects;

public record EmployeeId(String value) {

  public EmployeeId {
    final String normalizedValue =
        Objects.requireNonNull(value, "EmployeeId no puede ser nulo").trim();
    validateNotEmpty(normalizedValue);
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidEmployeeException.becauseIdIsEmpty();
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
