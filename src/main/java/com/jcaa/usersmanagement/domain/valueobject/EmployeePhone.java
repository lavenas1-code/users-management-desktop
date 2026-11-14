package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import java.util.Objects;
import java.util.regex.Pattern;

public record EmployeePhone(String value) {

  private static final int MINIMUM_DIGITS = 7;
  private static final int MAXIMUM_DIGITS = 15;
  private static final Pattern PHONE_PATTERN =
      Pattern.compile("^\\+?\\d{" + MINIMUM_DIGITS + "," + MAXIMUM_DIGITS + "}$");

  public EmployeePhone {
    final String normalizedValue =
        Objects.requireNonNull(value, "EmployeePhone no puede ser nulo").trim();
    validateFormat(normalizedValue);
    value = normalizedValue;
  }

  private static void validateFormat(final String normalizedValue) {
    if (!PHONE_PATTERN.matcher(normalizedValue).matches()) {
      throw InvalidEmployeeException.becausePhoneIsInvalid(MINIMUM_DIGITS, MAXIMUM_DIGITS);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
