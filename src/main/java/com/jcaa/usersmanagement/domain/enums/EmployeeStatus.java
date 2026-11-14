package com.jcaa.usersmanagement.domain.enums;

import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;

public enum EmployeeStatus {
  ACTIVE,
  INACTIVE;

  public static EmployeeStatus fromString(final String value) {
    for (final EmployeeStatus status : values()) {
      if (status.name().equalsIgnoreCase(value)) {
        return status;
      }
    }
    throw InvalidEmployeeException.becauseStatusIsInvalid(value);
  }
}
