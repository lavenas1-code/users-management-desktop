package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import java.util.Objects;

public record EmployeeLastName(String value) {

  private static final int MINIMUM_LENGTH = 2;

  public EmployeeLastName {
    final String normalizedValue =
        Objects.requireNonNull(value, "EmployeeLastName no puede ser nulo").trim();
    validateMinimumLength(normalizedValue);
    value = normalizedValue;
  }

  private static void validateMinimumLength(final String normalizedValue) {
    if (normalizedValue.length() < MINIMUM_LENGTH) {
      throw InvalidEmployeeException.becauseLastNameIsTooShort(MINIMUM_LENGTH);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
