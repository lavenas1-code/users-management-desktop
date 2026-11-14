package com.jcaa.usersmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import com.jcaa.usersmanagement.domain.enums.EmployeeStatus;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeAddress;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeBankAccount;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeFirstName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeId;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeLastName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeePhone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EmployeeModel")
class EmployeeModelTest {

  private static EmployeeModel buildNewEmployee() {
    return EmployeeModel.create(
        new EmployeeId("1047382910"),
        new EmployeeFirstName("Laura"),
        new EmployeeLastName("Gomez Perez"),
        new EmployeePhone("3001234567"),
        new EmployeeAddress("Calle 10 # 5-20"),
        new EmployeeBankAccount("12345678901"));
  }

  @Test
  @DisplayName("create() construye un empleado ACTIVE con los datos recibidos")
  void shouldCreateActiveEmployee() {
    final EmployeeModel employee = buildNewEmployee();

    assertAll(
        "empleado creado",
        () -> assertEquals("1047382910", employee.getId().value()),
        () -> assertEquals("Laura", employee.getFirstName().value()),
        () -> assertEquals("Gomez Perez", employee.getLastName().value()),
        () -> assertEquals("3001234567", employee.getPhone().value()),
        () -> assertEquals("Calle 10 # 5-20", employee.getAddress().value()),
        () -> assertEquals("12345678901", employee.getBankAccount().value()),
        () -> assertEquals(EmployeeStatus.ACTIVE, employee.getStatus()));
  }

  @Test
  @DisplayName("deactivate() devuelve una copia INACTIVE sin modificar el original")
  void shouldDeactivateEmployee() {
    final EmployeeModel employee = buildNewEmployee();

    final EmployeeModel deactivated = employee.deactivate();

    assertAll(
        "desactivacion",
        () -> assertNotSame(employee, deactivated),
        () -> assertEquals(EmployeeStatus.INACTIVE, deactivated.getStatus()),
        () -> assertEquals(EmployeeStatus.ACTIVE, employee.getStatus()),
        () -> assertEquals(employee.getId(), deactivated.getId()));
  }

  @Test
  @DisplayName("activate() devuelve una copia ACTIVE a partir de un empleado INACTIVE")
  void shouldActivateEmployee() {
    final EmployeeModel inactive = buildNewEmployee().deactivate();

    final EmployeeModel activated = inactive.activate();

    assertEquals(EmployeeStatus.ACTIVE, activated.getStatus());
  }
}
