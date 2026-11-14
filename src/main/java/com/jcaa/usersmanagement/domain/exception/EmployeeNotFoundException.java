package com.jcaa.usersmanagement.domain.exception;

public final class EmployeeNotFoundException extends DomainException {

  private static final String MESSAGE_BY_ID = "No se encontró el empleado con id '%s'.";

  private EmployeeNotFoundException(final String message) {
    super(message);
  }

  public static EmployeeNotFoundException becauseIdWasNotFound(final String employeeId) {
    return new EmployeeNotFoundException(String.format(MESSAGE_BY_ID, employeeId));
  }
}
