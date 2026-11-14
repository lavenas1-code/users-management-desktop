package com.jcaa.usersmanagement.domain.exception;

public final class EmployeeAlreadyExistsException extends DomainException {

  private static final String MESSAGE_ID_EXISTS = "Ya existe un empleado con id '%s'.";

  private EmployeeAlreadyExistsException(final String message) {
    super(message);
  }

  public static EmployeeAlreadyExistsException becauseIdAlreadyExists(final String employeeId) {
    return new EmployeeAlreadyExistsException(String.format(MESSAGE_ID_EXISTS, employeeId));
  }
}
