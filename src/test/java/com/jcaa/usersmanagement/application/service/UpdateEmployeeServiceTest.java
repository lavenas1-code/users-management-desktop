package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.GetEmployeeByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateEmployeePort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmployeeCommand;
import com.jcaa.usersmanagement.domain.exception.EmployeeNotFoundException;
import com.jcaa.usersmanagement.domain.exception.InvalidEmployeeException;
import com.jcaa.usersmanagement.domain.enums.EmployeeStatus;
import com.jcaa.usersmanagement.domain.model.EmployeeModel;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeAddress;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeBankAccount;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeFirstName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeId;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeLastName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeePhone;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests para UpdateEmployeeService: validacion, empleado inexistente y flujo feliz. */
@DisplayName("UpdateEmployeeService")
@ExtendWith(MockitoExtension.class)
class UpdateEmployeeServiceTest {

  @Mock private UpdateEmployeePort updateEmployeePort;
  @Mock private GetEmployeeByIdPort getEmployeeByIdPort;

  private UpdateEmployeeService service;

  private static EmployeeModel buildEmployee(final String id, final EmployeeStatus status) {
    return new EmployeeModel(
        new EmployeeId(id),
        new EmployeeFirstName("Laura"),
        new EmployeeLastName("Gomez Perez"),
        new EmployeePhone("3001234567"),
        new EmployeeAddress("Calle 10 # 5-20"),
        new EmployeeBankAccount("12345678901"),
        status);
  }

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new UpdateEmployeeService(
              updateEmployeePort, getEmployeeByIdPort, validatorFactory.getValidator());
    }
  }

  @Test
  @DisplayName("execute() actualiza el empleado existente con los nuevos datos")
  void shouldUpdateExistingEmployee() {
    // Arrange
    final UpdateEmployeeCommand command =
        new UpdateEmployeeCommand(
            "1047382910",
            "Laura Maria",
            "Gomez Perez",
            "3119998877",
            "Carrera 7 # 12-30",
            "99999999999",
            "INACTIVE");
    final EmployeeModel updated = buildEmployee("1047382910", EmployeeStatus.INACTIVE);
    final ArgumentCaptor<EmployeeModel> captor = ArgumentCaptor.forClass(EmployeeModel.class);
    when(getEmployeeByIdPort.getById(any()))
        .thenReturn(Optional.of(buildEmployee("1047382910", EmployeeStatus.ACTIVE)));
    when(updateEmployeePort.update(captor.capture())).thenReturn(updated);

    // Act
    final EmployeeModel result = service.execute(command);

    // Assert
    assertAll(
        "flujo feliz de UpdateEmployeeService",
        () -> assertEquals(EmployeeStatus.INACTIVE, result.getStatus()),
        () -> assertEquals("Laura Maria", captor.getValue().getFirstName().value()),
        () -> assertEquals("3119998877", captor.getValue().getPhone().value()),
        () -> assertEquals("Carrera 7 # 12-30", captor.getValue().getAddress().value()),
        () -> assertEquals("99999999999", captor.getValue().getBankAccount().value()),
        () -> assertEquals(EmployeeStatus.INACTIVE, captor.getValue().getStatus()));
  }

  @Test
  @DisplayName("execute() lanza EmployeeNotFoundException cuando el empleado no existe")
  void shouldThrowWhenEmployeeDoesNotExist() {
    // Arrange
    final UpdateEmployeeCommand command =
        new UpdateEmployeeCommand(
            "0000000000",
            "Laura",
            "Gomez Perez",
            "3001234567",
            "Calle 10 # 5-20",
            "12345678901",
            "ACTIVE");
    when(getEmployeeByIdPort.getById(any())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(EmployeeNotFoundException.class, () -> service.execute(command));
    verify(updateEmployeePort, never()).update(any());
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el command es invalido")
  void shouldThrowWhenCommandIsInvalid() {
    // Arrange
    final UpdateEmployeeCommand command =
        new UpdateEmployeeCommand("", "L", "G", "", "", "", "");

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(updateEmployeePort, getEmployeeByIdPort);
  }

  @Test
  @DisplayName("execute() lanza InvalidEmployeeException cuando el estado no es valido")
  void shouldThrowWhenStatusIsInvalid() {
    // Arrange
    final UpdateEmployeeCommand command =
        new UpdateEmployeeCommand(
            "1047382910",
            "Laura",
            "Gomez Perez",
            "3001234567",
            "Calle 10 # 5-20",
            "12345678901",
            "BLOCKED");
    when(getEmployeeByIdPort.getById(any()))
        .thenReturn(Optional.of(buildEmployee("1047382910", EmployeeStatus.ACTIVE)));

    // Act & Assert
    assertThrows(InvalidEmployeeException.class, () -> service.execute(command));
    verify(updateEmployeePort, never()).update(any());
  }
}
