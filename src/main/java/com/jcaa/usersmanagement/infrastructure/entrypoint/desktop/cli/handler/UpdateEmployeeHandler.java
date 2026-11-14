package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.EmployeeNotFoundException;
import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.EmployeeResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.EmployeeController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.EmployeeResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateEmployeeRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateEmployeeHandler implements OperationHandler {

  private final EmployeeController employeeController;
  private final ConsoleIO console;
  private final EmployeeResponsePrinter printer;

  @Override
  public void handle() {
    final String id          = console.readRequired("ID del empleado (DNI)           : ");
    final String firstName   = console.readRequired("Nuevo nombre                    : ");
    final String lastName    = console.readRequired("Nuevo apellido                  : ");
    final String phone       = console.readRequired("Nuevo teléfono                  : ");
    final String address     = console.readRequired("Nueva dirección                 : ");
    final String bankAccount = console.readRequired("Nueva cuenta bancaria (dígitos) : ");
    final String status      = console.readRequired("Estado (ACTIVE / INACTIVE)      : ");

    try {
      final EmployeeResponse updated =
          employeeController.updateEmployee(
              new UpdateEmployeeRequest(
                  id, firstName, lastName, phone, address, bankAccount, status));
      console.println("\n  Empleado actualizado correctamente.");
      printer.print(updated);
    } catch (final EmployeeNotFoundException | InvalidEmployeeException exception) {
      console.println("  Error: " + exception.getMessage());
    }
  }
}
