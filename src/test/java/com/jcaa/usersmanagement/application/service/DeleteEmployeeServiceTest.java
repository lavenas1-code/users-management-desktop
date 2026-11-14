package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.DeleteEmployeePort;
import com.jcaa.usersmanagement.application.port.out.GetEmployeeByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmployeeCommand;
import com.jcaa.usersmanagement.domain.exception.EmployeeNotFoundException;
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

/** Tests para DeleteEmployeeService: validacion, empleado inexistente y flujo feliz. */
@DisplayName("DeleteEmployeeService")
@ExtendWith(MockitoExtension.class)
class DeleteEmployeeServiceTest {

  @Mock private DeleteEmployeePort deleteEmployeePort;
  @Mock private GetEmployeeByIdPort getEmployeeByIdPort;

  private DeleteEmployeeService service;

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
          new DeleteEmployeeService(
              deleteEmployeePort, getEmployeeByIdPort, validatorFactory.getValidator());
    }
  }

  @Test
  @DisplayName("execute() elimina el empleado cuando existe")
  void shouldDeleteEmployeeWhenItExists() {
    // Arrange
    final ArgumentCaptor<EmployeeId> captor = ArgumentCaptor.forClass(EmployeeId.class);
    when(getEmployeeByIdPort.getById(any()))
        .thenReturn(Optional.of(buildEmployee("1047382910", EmployeeStatus.ACTIVE)));

    // Act
    service.execute(new DeleteEmployeeCommand("1047382910"));

    // Assert
    verify(deleteEmployeePort).delete(captor.capture());
    assertEquals("1047382910", captor.getValue().value());
  }

  @Test
  @DisplayName("execute() lanza EmployeeNotFoundException cuando el empleado no existe")
  void shouldThrowWhenEmployeeDoesNotExist() {
    // Arrange
    when(getEmployeeByIdPort.getById(any())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        EmployeeNotFoundException.class,
        () -> service.execute(new DeleteEmployeeCommand("0000000000")));
    verify(deleteEmployeePort, never()).delete(any());
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el id esta en blanco")
  void shouldThrowWhenCommandIsInvalid() {
    // Act & Assert
    assertThrows(
        ConstraintViolationException.class, () -> service.execute(new DeleteEmployeeCommand(" ")));
    verifyNoInteractions(deleteEmployeePort, getEmployeeByIdPort);
  }
}
