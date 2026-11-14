package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.EmployeeAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.EmployeeResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.EmployeeController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateEmployeeRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.EmployeeResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateEmployeeHandler implements OperationHandler {

  private final EmployeeController employeeController;
  private final ConsoleIO console;
  private final EmployeeResponsePrinter printer;

  @Override
  public void handle() {
    final String id          = console.readRequired("ID (DNI)                      : ");
    final String firstName   = console.readRequired("Nombre                        : ");
    final String lastName    = console.readRequired("Apellido                      : ");
    final String phone       = console.readRequired("Teléfono                      : ");
    final String address     = console.readRequired("Dirección                     : ");
    final String bankAccount = console.readRequired("Cuenta bancaria (dígitos)     : ");

    try {
      final EmployeeResponse created =
          employeeController.createEmployee(
              new CreateEmployeeRequest(id, firstName, lastName, phone, address, bankAccount));
      console.println("\n  Empleado creado correctamente.");
      printer.print(created);
    } catch (final EmployeeAlreadyExistsException | InvalidEmployeeException exception) {
      console.println("  Error: " + exception.getMessage());
    }
  }
}
