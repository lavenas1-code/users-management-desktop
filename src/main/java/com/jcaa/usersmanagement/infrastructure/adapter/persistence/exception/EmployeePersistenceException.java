package com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception;

public final class EmployeePersistenceException extends RuntimeException {

  private static final String MESSAGE_SAVE = "No se pudo guardar el empleado con ID: '%s'.";
  private static final String MESSAGE_UPDATE = "No se pudo actualizar el empleado con ID: '%s'.";
  private static final String MESSAGE_FIND = "No se pudo consultar el empleado con ID: '%s'.";
  private static final String MESSAGE_ALL = "No se pudo obtener la lista de empleados.";
  private static final String MESSAGE_DELETE = "No se pudo eliminar el empleado con ID: '%s'.";

  private EmployeePersistenceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static EmployeePersistenceException becauseSaveFailed(
      final String employeeId, final Throwable cause) {
    return new EmployeePersistenceException(String.format(MESSAGE_SAVE, employeeId), cause);
  }

  public static EmployeePersistenceException becauseUpdateFailed(
      final String employeeId, final Throwable cause) {
    return new EmployeePersistenceException(String.format(MESSAGE_UPDATE, employeeId), cause);
  }

  public static EmployeePersistenceException becauseFindByIdFailed(
      final String employeeId, final Throwable cause) {
    return new EmployeePersistenceException(String.format(MESSAGE_FIND, employeeId), cause);
  }

  public static EmployeePersistenceException becauseFindAllFailed(final Throwable cause) {
    return new EmployeePersistenceException(MESSAGE_ALL, cause);
  }

  public static EmployeePersistenceException becauseDeleteFailed(
      final String employeeId, final Throwable cause) {
    return new EmployeePersistenceException(String.format(MESSAGE_DELETE, employeeId), cause);
  }
}
