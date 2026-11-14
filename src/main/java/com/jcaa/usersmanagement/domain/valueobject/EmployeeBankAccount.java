package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import java.util.Objects;
import java.util.regex.Pattern;

public record EmployeeBankAccount(String value) {

  private static final int MINIMUM_DIGITS = 8;
  private static final int MAXIMUM_DIGITS = 20;
  private static final Pattern ACCOUNT_PATTERN =
      Pattern.compile("^\\d{" + MINIMUM_DIGITS + "," + MAXIMUM_DIGITS + "}$");

  public EmployeeBankAccount {
    final String normalizedValue =
        Objects.requireNonNull(value, "EmployeeBankAccount no puede ser nulo").trim();
    validateFormat(normalizedValue);
    value = normalizedValue;
  }

  private static void validateFormat(final String normalizedValue) {
    if (!ACCOUNT_PATTERN.matcher(normalizedValue).matches()) {
      throw InvalidEmployeeException.becauseBankAccountIsInvalid(MINIMUM_DIGITS, MAXIMUM_DIGITS);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
