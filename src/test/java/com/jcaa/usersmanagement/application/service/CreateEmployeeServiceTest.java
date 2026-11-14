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
import com.jcaa.usersmanagement.application.port.out.SaveEmployeePort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateEmployeeCommand;
import com.jcaa.usersmanagement.domain.exception.EmployeeAlreadyExistsException;
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

/** Tests para CreateEmployeeService: validacion, id duplicado y flujo feliz. */
@DisplayName("CreateEmployeeService")
@ExtendWith(MockitoExtension.class)
class CreateEmployeeServiceTest {

  @Mock private SaveEmployeePort saveEmployeePort;
  @Mock private GetEmployeeByIdPort getEmployeeByIdPort;

  private CreateEmployeeService service;

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
          new CreateEmployeeService(
              saveEmployeePort, getEmployeeByIdPort, validatorFactory.getValidator());
    }
  }

  @Test
  @DisplayName("execute() guarda el empleado cuando el id no existe")
  void shouldSaveEmployeeWhenIdIsNew() {
    // Arrange
    final CreateEmployeeCommand command =
        new CreateEmployeeCommand(
            "1047382910", "Laura", "Gomez Perez", "3001234567", "Calle 10 # 5-20", "12345678901");
    final EmployeeModel saved = buildEmployee("1047382910", EmployeeStatus.ACTIVE);
    final ArgumentCaptor<EmployeeModel> captor = ArgumentCaptor.forClass(EmployeeModel.class);
    when(getEmployeeByIdPort.getById(any())).thenReturn(Optional.empty());
    when(saveEmployeePort.save(captor.capture())).thenReturn(saved);

    // Act
    final EmployeeModel result = service.execute(command);

    // Assert
    assertAll(
        "flujo feliz de CreateEmployeeService",
        () -> assertEquals("1047382910", result.getId().value()),
        () -> assertEquals(EmployeeStatus.ACTIVE, captor.getValue().getStatus()),
        () -> assertEquals("Laura", captor.getValue().getFirstName().value()),
        () -> assertEquals("12345678901", captor.getValue().getBankAccount().value()));
  }

  @Test
  @DisplayName("execute() lanza EmployeeAlreadyExistsException cuando el id ya esta registrado")
  void shouldThrowWhenIdAlreadyExists() {
    // Arrange
    final CreateEmployeeCommand command =
        new CreateEmployeeCommand(
            "1047382910", "Laura", "Gomez Perez", "3001234567", "Calle 10 # 5-20", "12345678901");
    when(getEmployeeByIdPort.getById(any()))
        .thenReturn(Optional.of(buildEmployee("1047382910", EmployeeStatus.ACTIVE)));

    // Act & Assert
    assertThrows(EmployeeAlreadyExistsException.class, () -> service.execute(command));
    verify(saveEmployeePort, never()).save(any());
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el command es invalido")
  void shouldThrowWhenCommandIsInvalid() {
    // Arrange - id en blanco, nombre corto, resto vacio
    final CreateEmployeeCommand command = new CreateEmployeeCommand("", "L", "G", "", "", "");

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(saveEmployeePort, getEmployeeByIdPort);
  }

  @Test
  @DisplayName("execute() lanza InvalidEmployeeException cuando el telefono tiene formato invalido")
  void shouldThrowWhenPhoneFormatIsInvalid() {
    // Arrange - pasa Bean Validation pero rompe la regla del value object
    final CreateEmployeeCommand command =
        new CreateEmployeeCommand(
            "1047382910", "Laura", "Gomez Perez", "abc", "Calle 10 # 5-20", "12345678901");
    when(getEmployeeByIdPort.getById(any())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(InvalidEmployeeException.class, () -> service.execute(command));
    verify(saveEmployeePort, never()).save(any());
  }
}
