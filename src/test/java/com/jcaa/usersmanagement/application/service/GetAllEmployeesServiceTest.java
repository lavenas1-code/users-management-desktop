package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.GetAllEmployeesPort;
import com.jcaa.usersmanagement.domain.enums.EmployeeStatus;
import com.jcaa.usersmanagement.domain.model.EmployeeModel;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeAddress;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeBankAccount;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeFirstName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeId;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeLastName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeePhone;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests para GetAllEmployeesService: delega en el puerto de salida. */
@DisplayName("GetAllEmployeesService")
@ExtendWith(MockitoExtension.class)
class GetAllEmployeesServiceTest {

  @Mock private GetAllEmployeesPort getAllEmployeesPort;

  @InjectMocks private GetAllEmployeesService service;

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

  @Test
  @DisplayName("execute() devuelve los empleados entregados por el puerto")
  void shouldReturnEmployeesFromPort() {
    // Arrange
    final List<EmployeeModel> employees =
        List.of(
            buildEmployee("1047382910", EmployeeStatus.ACTIVE),
            buildEmployee("1098765432", EmployeeStatus.INACTIVE));
    when(getAllEmployeesPort.getAll()).thenReturn(employees);

    // Act
    final List<EmployeeModel> result = service.execute();

    // Assert
    assertEquals(2, result.size());
    verify(getAllEmployeesPort).getAll();
  }

  @Test
  @DisplayName("execute() devuelve una lista vacia cuando no hay empleados")
  void shouldReturnEmptyListWhenThereAreNoEmployees() {
    // Arrange
    when(getAllEmployeesPort.getAll()).thenReturn(List.of());

    // Act & Assert
    assertTrue(service.execute().isEmpty());
  }
}
