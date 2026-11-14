package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.in.CreateEmployeeUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteEmployeeUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllEmployeesUseCase;
import com.jcaa.usersmanagement.application.port.in.GetEmployeeByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateEmployeeUseCase;
import com.jcaa.usersmanagement.application.service.dto.command.CreateEmployeeCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmployeeCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmployeeCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmployeeByIdQuery;
import com.jcaa.usersmanagement.domain.enums.EmployeeStatus;
import com.jcaa.usersmanagement.domain.exception.EmployeeAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.EmployeeNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmployeeModel;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeAddress;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeBankAccount;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeFirstName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeId;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeLastName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeePhone;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateEmployeeRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.EmployeeResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateEmployeeRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests para EmployeeController.
 *
 * <p>Cubre: delegación a cada puerto de entrada, mapeo DTO a command/query, mapeo del modelo de
 * dominio a response y propagación de excepciones. Todos los puertos están mockeados.
 */
@DisplayName("EmployeeController")
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

  @Mock private CreateEmployeeUseCase createEmployeeUseCase;
  @Mock private UpdateEmployeeUseCase updateEmployeeUseCase;
  @Mock private DeleteEmployeeUseCase deleteEmployeeUseCase;
  @Mock private GetEmployeeByIdUseCase getEmployeeByIdUseCase;
  @Mock private GetAllEmployeesUseCase getAllEmployeesUseCase;

  private EmployeeController controller;

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
    controller =
        new EmployeeController(
            createEmployeeUseCase,
            updateEmployeeUseCase,
            deleteEmployeeUseCase,
            getEmployeeByIdUseCase,
            getAllEmployeesUseCase);
  }

  // ── listAllEmployees

  @Test
  @DisplayName("listAllEmployees() devuelve la lista de EmployeeResponse mapeada")
  void listAllEmployees_returnsMappedList_whenEmployeesExist() {
    // Arrange
    when(getAllEmployeesUseCase.execute())
        .thenReturn(List.of(buildEmployee("1047382910", EmployeeStatus.ACTIVE)));

    // Act
    final List<EmployeeResponse> result = controller.listAllEmployees();

    // Assert
    assertAll(
        "mapeo de la lista con un solo empleado",
        () -> assertEquals(1, result.size()),
        () -> assertEquals("1047382910", result.get(0).id()),
        () -> assertEquals("Laura", result.get(0).firstName()),
        () -> assertEquals("Gomez Perez", result.get(0).lastName()),
        () -> assertEquals("3001234567", result.get(0).phone()),
        () -> assertEquals("Calle 10 # 5-20", result.get(0).address()),
        () -> assertEquals("12345678901", result.get(0).bankAccount()),
        () -> assertEquals("ACTIVE", result.get(0).status()));
    verify(getAllEmployeesUseCase).execute();
  }

  @Test
  @DisplayName("listAllEmployees() devuelve una lista vacía cuando no hay empleados")
  void listAllEmployees_returnsEmptyList_whenNoEmployeesExist() {
    // Arrange
    when(getAllEmployeesUseCase.execute()).thenReturn(List.of());

    // Act & Assert
    assertTrue(controller.listAllEmployees().isEmpty());
  }

  // ── findEmployeeById

  @Test
  @DisplayName("findEmployeeById() construye el query con el id dado y mapea la respuesta")
  void findEmployeeById_returnsMappedResponse_whenEmployeeExists() {
    // Arrange
    when(getEmployeeByIdUseCase.execute(new GetEmployeeByIdQuery("1098765432")))
        .thenReturn(buildEmployee("1098765432", EmployeeStatus.INACTIVE));

    // Act
    final EmployeeResponse result = controller.findEmployeeById("1098765432");

    // Assert
    assertAll(
        "mapeo de la respuesta de findEmployeeById",
        () -> assertEquals("1098765432", result.id()),
        () -> assertEquals("INACTIVE", result.status()));
  }

  @Test
  @DisplayName("findEmployeeById() propaga EmployeeNotFoundException")
  void findEmployeeById_propagatesNotFound_whenEmployeeDoesNotExist() {
    // Arrange
    when(getEmployeeByIdUseCase.execute(new GetEmployeeByIdQuery("999")))
        .thenThrow(EmployeeNotFoundException.becauseIdWasNotFound("999"));

    // Act & Assert
    assertThrows(EmployeeNotFoundException.class, () -> controller.findEmployeeById("999"));
  }

  // ── createEmployee

  @Test
  @DisplayName("createEmployee() delega un CreateEmployeeCommand completo y mapea el resultado")
  void createEmployee_delegatesCommandAndMapsResponse() {
    // Arrange
    final CreateEmployeeRequest request =
        new CreateEmployeeRequest(
            "1047382910", "Laura", "Gomez Perez", "3001234567", "Calle 10 # 5-20", "12345678901");
    final ArgumentCaptor<CreateEmployeeCommand> captor =
        ArgumentCaptor.forClass(CreateEmployeeCommand.class);
    when(createEmployeeUseCase.execute(captor.capture()))
        .thenReturn(buildEmployee("1047382910", EmployeeStatus.ACTIVE));

    // Act
    final EmployeeResponse result = controller.createEmployee(request);

    // Assert
    assertAll(
        "delegación del command y mapeo de la respuesta de createEmployee",
        () -> assertEquals("1047382910", captor.getValue().id()),
        () -> assertEquals("Laura", captor.getValue().firstName()),
        () -> assertEquals("Gomez Perez", captor.getValue().lastName()),
        () -> assertEquals("3001234567", captor.getValue().phone()),
        () -> assertEquals("Calle 10 # 5-20", captor.getValue().address()),
        () -> assertEquals("12345678901", captor.getValue().bankAccount()),
        () -> assertEquals("1047382910", result.id()),
        () -> assertEquals("ACTIVE", result.status()));
  }

  @Test
  @DisplayName("createEmployee() propaga EmployeeAlreadyExistsException")
  void createEmployee_propagatesAlreadyExists_whenIdIsDuplicated() {
    // Arrange
    final CreateEmployeeRequest request =
        new CreateEmployeeRequest(
            "1047382910", "Laura", "Gomez Perez", "3001234567", "Calle 10 # 5-20", "12345678901");
    when(createEmployeeUseCase.execute(any()))
        .thenThrow(EmployeeAlreadyExistsException.becauseIdAlreadyExists("1047382910"));

    // Act & Assert
    assertThrows(EmployeeAlreadyExistsException.class, () -> controller.createEmployee(request));
  }

  // ── updateEmployee

  @Test
  @DisplayName("updateEmployee() delega un UpdateEmployeeCommand completo y mapea el resultado")
  void updateEmployee_delegatesCommandAndMapsResponse() {
    // Arrange
    final UpdateEmployeeRequest request =
        new UpdateEmployeeRequest(
            "1047382910",
            "Laura",
            "Gomez Perez",
            "3001234567",
            "Calle 10 # 5-20",
            "12345678901",
            "INACTIVE");
    final ArgumentCaptor<UpdateEmployeeCommand> captor =
        ArgumentCaptor.forClass(UpdateEmployeeCommand.class);
    when(updateEmployeeUseCase.execute(captor.capture()))
        .thenReturn(buildEmployee("1047382910", EmployeeStatus.INACTIVE));

    // Act
    final EmployeeResponse result = controller.updateEmployee(request);

    // Assert
    assertAll(
        "delegación del command y mapeo de la respuesta de updateEmployee",
        () -> assertEquals("1047382910", captor.getValue().id()),
        () -> assertEquals("INACTIVE", captor.getValue().status()),
        () -> assertEquals("12345678901", captor.getValue().bankAccount()),
        () -> assertEquals("INACTIVE", result.status()));
  }

  @Test
  @DisplayName("updateEmployee() propaga EmployeeNotFoundException")
  void updateEmployee_propagatesNotFound_whenEmployeeDoesNotExist() {
    // Arrange
    final UpdateEmployeeRequest request =
        new UpdateEmployeeRequest(
            "999", "Laura", "Gomez Perez", "3001234567", "Calle 10", "12345678901", "ACTIVE");
    when(updateEmployeeUseCase.execute(any()))
        .thenThrow(EmployeeNotFoundException.becauseIdWasNotFound("999"));

    // Act & Assert
    assertThrows(EmployeeNotFoundException.class, () -> controller.updateEmployee(request));
  }

  // ── deleteEmployee

  @Test
  @DisplayName("deleteEmployee() delega un DeleteEmployeeCommand con el id dado")
  void deleteEmployee_delegatesDeleteCommandWithCorrectId() {
    // Arrange
    final ArgumentCaptor<DeleteEmployeeCommand> captor =
        ArgumentCaptor.forClass(DeleteEmployeeCommand.class);
    doNothing().when(deleteEmployeeUseCase).execute(captor.capture());

    // Act
    controller.deleteEmployee("1047382910");

    // Assert
    assertEquals("1047382910", captor.getValue().id());
  }

  @Test
  @DisplayName("deleteEmployee() propaga EmployeeNotFoundException")
  void deleteEmployee_propagatesNotFound_whenEmployeeDoesNotExist() {
    // Arrange
    doThrow(EmployeeNotFoundException.becauseIdWasNotFound("999"))
        .when(deleteEmployeeUseCase)
        .execute(any());

    // Act & Assert
    assertThrows(EmployeeNotFoundException.class, () -> controller.deleteEmployee("999"));
  }
}
