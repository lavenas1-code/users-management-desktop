package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.GetEmployeeByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmployeeByIdQuery;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests para GetEmployeeByIdService: validacion, empleado inexistente y flujo feliz. */
@DisplayName("GetEmployeeByIdService")
@ExtendWith(MockitoExtension.class)
class GetEmployeeByIdServiceTest {

  @Mock private GetEmployeeByIdPort getEmployeeByIdPort;

  private GetEmployeeByIdService service;

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
      service = new GetEmployeeByIdService(getEmployeeByIdPort, validatorFactory.getValidator());
    }
  }

  @Test
  @DisplayName("execute() devuelve el empleado cuando existe")
  void shouldReturnEmployeeWhenItExists() {
    // Arrange
    final EmployeeModel employee = buildEmployee("1047382910", EmployeeStatus.ACTIVE);
    when(getEmployeeByIdPort.getById(any())).thenReturn(Optional.of(employee));

    // Act
    final EmployeeModel result = service.execute(new GetEmployeeByIdQuery("1047382910"));

    // Assert
    assertEquals("1047382910", result.getId().value());
  }

  @Test
  @DisplayName("execute() lanza EmployeeNotFoundException cuando el empleado no existe")
  void shouldThrowWhenEmployeeDoesNotExist() {
    // Arrange
    when(getEmployeeByIdPort.getById(any())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        EmployeeNotFoundException.class,
        () -> service.execute(new GetEmployeeByIdQuery("0000000000")));
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el id esta en blanco")
  void shouldThrowWhenQueryIsInvalid() {
    // Act & Assert
    assertThrows(
        ConstraintViolationException.class,
        () -> service.execute(new GetEmployeeByIdQuery("")));
    verifyNoInteractions(getEmployeeByIdPort);
  }
}
