package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import java.util.Objects;

public record EmployeeFirstName(String value) {

  private static final int MINIMUM_LENGTH = 2;

  public EmployeeFirstName {
    final String normalizedValue =
        Objects.requireNonNull(value, "EmployeeFirstName no puede ser nulo").trim();
    validateMinimumLength(normalizedValue);
    value = normalizedValue;
  }

  private static void validateMinimumLength(final String normalizedValue) {
    if (normalizedValue.length() < MINIMUM_LENGTH) {
      throw InvalidEmployeeException.becauseFirstNameIsTooShort(MINIMUM_LENGTH);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
