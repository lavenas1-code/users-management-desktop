package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.CreateEmployeeHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.DeleteEmployeeHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.FindEmployeeByIdHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.ListEmployeesHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.OperationHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.UpdateEmployeeHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.EmployeeResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu.EmployeeMenuOption;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.EmployeeController;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class EmployeeManagementCli {

  private static final String MENU_BORDER = "  ==========================================";

  private final EmployeeController employeeController;
  private final ConsoleIO console;

  public void start() {
    final EmployeeResponsePrinter printer = new EmployeeResponsePrinter(console);
    runLoop(buildHandlers(printer));
  }

  private void runLoop(final Map<EmployeeMenuOption, OperationHandler> handlers) {
    boolean running = true;
    while (running) {
      printMenu();
      final int choice = console.readInt("\n  Opción: ");
      final Optional<EmployeeMenuOption> option = EmployeeMenuOption.fromNumber(choice);

      if (option.isEmpty()) {
        console.println("  Opción inválida. Intenta de nuevo.");
      } else if (option.get() == EmployeeMenuOption.BACK) {
        running = false;
      } else {
        executeHandler(handlers, option.get());
      }
    }
  }

  private void executeHandler(
      final Map<EmployeeMenuOption, OperationHandler> handlers, final EmployeeMenuOption option) {
    try {
      handlers.get(option).handle();
    } catch (final ConstraintViolationException exception) {
      console.println("  Errores de validación:");
      exception.getConstraintViolations()
          .forEach(violation -> console.println("    - " + violation.getMessage()));
    } catch (final RuntimeException exception) {
      console.println("  Error inesperado: " + exception.getMessage());
    }
  }

  private Map<EmployeeMenuOption, OperationHandler> buildHandlers(
      final EmployeeResponsePrinter printer) {
    return Map.of(
        EmployeeMenuOption.LIST_EMPLOYEES,  new ListEmployeesHandler(employeeController, printer),
        EmployeeMenuOption.FIND_EMPLOYEE,   new FindEmployeeByIdHandler(employeeController, console, printer),
        EmployeeMenuOption.CREATE_EMPLOYEE, new CreateEmployeeHandler(employeeController, console, printer),
        EmployeeMenuOption.UPDATE_EMPLOYEE, new UpdateEmployeeHandler(employeeController, console, printer),
        EmployeeMenuOption.DELETE_EMPLOYEE, new DeleteEmployeeHandler(employeeController, console));
  }

  private void printMenu() {
    console.println();
    console.println(MENU_BORDER);
    console.println("    Menú de Empleados");
    console.println(MENU_BORDER);
    for (final EmployeeMenuOption option : EmployeeMenuOption.values()) {
      console.printf("    [%d] %s%n", option.getNumber(), option.getDescription());
    }
    console.println(MENU_BORDER);
  }
}
