package com.jcaa.usersmanagement.domain.exception;

public final class InvalidEmployeeException extends DomainException {

  private static final String MESSAGE_ID_EMPTY = "El id (DNI) del empleado no puede estar vacío.";
  private static final String MESSAGE_FIRST_NAME_INVALID =
      "El nombre del empleado debe tener al menos %d caracteres.";
  private static final String MESSAGE_LAST_NAME_INVALID =
      "El apellido del empleado debe tener al menos %d caracteres.";
  private static final String MESSAGE_PHONE_INVALID =
      "El teléfono del empleado debe contener entre %d y %d dígitos (con '+' inicial opcional).";
  private static final String MESSAGE_ADDRESS_EMPTY = "La dirección del empleado no puede estar vacía.";
  private static final String MESSAGE_BANK_ACCOUNT_INVALID =
      "La cuenta bancaria del empleado debe contener entre %d y %d dígitos.";
  private static final String MESSAGE_STATUS_INVALID = "El estado '%s' del empleado no es válido.";

  private InvalidEmployeeException(final String message) {
    super(message);
  }

  public static InvalidEmployeeException becauseIdIsEmpty() {
    return new InvalidEmployeeException(MESSAGE_ID_EMPTY);
  }

  public static InvalidEmployeeException becauseFirstNameIsTooShort(final int minimumLength) {
    return new InvalidEmployeeException(String.format(MESSAGE_FIRST_NAME_INVALID, minimumLength));
  }

  public static InvalidEmployeeException becauseLastNameIsTooShort(final int minimumLength) {
    return new InvalidEmployeeException(String.format(MESSAGE_LAST_NAME_INVALID, minimumLength));
  }

  public static InvalidEmployeeException becausePhoneIsInvalid(
      final int minimumDigits, final int maximumDigits) {
    return new InvalidEmployeeException(
        String.format(MESSAGE_PHONE_INVALID, minimumDigits, maximumDigits));
  }

  public static InvalidEmployeeException becauseAddressIsEmpty() {
    return new InvalidEmployeeException(MESSAGE_ADDRESS_EMPTY);
  }

  public static InvalidEmployeeException becauseBankAccountIsInvalid(
      final int minimumDigits, final int maximumDigits) {
    return new InvalidEmployeeException(
        String.format(MESSAGE_BANK_ACCOUNT_INVALID, minimumDigits, maximumDigits));
  }

  public static InvalidEmployeeException becauseStatusIsInvalid(final String value) {
    return new InvalidEmployeeException(String.format(MESSAGE_STATUS_INVALID, value));
  }
}
