package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.EmployeeManagementCli;
import lombok.RequiredArgsConstructor;

/** Abre el submenú de empleados desde el menú principal de la CLI. */
@RequiredArgsConstructor
public final class ManageEmployeesHandler implements OperationHandler {

  private final EmployeeManagementCli employeeManagementCli;

  @Override
  public void handle() {
    employeeManagementCli.start();
  }
}
