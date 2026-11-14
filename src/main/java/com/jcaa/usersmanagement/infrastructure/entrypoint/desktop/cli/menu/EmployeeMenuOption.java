package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu;

import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmployeeMenuOption {

  LIST_EMPLOYEES(1, "Listar todos los empleados"),
  FIND_EMPLOYEE(2, "Buscar empleado por ID"),
  CREATE_EMPLOYEE(3, "Crear empleado"),
  UPDATE_EMPLOYEE(4, "Actualizar empleado"),
  DELETE_EMPLOYEE(5, "Eliminar empleado"),
  BACK(0, "Volver al menú principal");

  private final int number;
  private final String description;

  public static Optional<EmployeeMenuOption> fromNumber(final int number) {
    for (final EmployeeMenuOption option : values()) {
      if (option.number == number) {
        return Optional.of(option);
      }
    }
    return Optional.empty();
  }
}
