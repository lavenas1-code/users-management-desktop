package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.EmployeeResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class EmployeeResponsePrinter {

  private static final String SEPARATOR = "-".repeat(52);
  private static final String ROW_FORMAT = "  %-16s : %s%n";

  private final ConsoleIO console;

  public void print(final EmployeeResponse response) {
    console.println(SEPARATOR);
    console.printf(ROW_FORMAT, "ID (DNI)",        response.id());
    console.printf(ROW_FORMAT, "Nombre",           response.firstName());
    console.printf(ROW_FORMAT, "Apellido",         response.lastName());
    console.printf(ROW_FORMAT, "Teléfono",         response.phone());
    console.printf(ROW_FORMAT, "Dirección",        response.address());
    console.printf(ROW_FORMAT, "Cuenta bancaria", response.bankAccount());
    console.printf(ROW_FORMAT, "Estado",          response.status());
    console.println(SEPARATOR);
  }

  public void printList(final List<EmployeeResponse> employees) {
    if (employees.isEmpty()) {
      console.println("  No se encontraron empleados.");
      return;
    }
    console.printf("%n  Total: %d empleado(s)%n", employees.size());
    employees.forEach(this::print);
  }
}
