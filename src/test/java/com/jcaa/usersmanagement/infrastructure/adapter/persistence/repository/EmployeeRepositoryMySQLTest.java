package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.enums.EmployeeStatus;
import com.jcaa.usersmanagement.domain.model.EmployeeModel;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeAddress;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeBankAccount;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeFirstName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeId;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeLastName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeePhone;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.EmployeePersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests para EmployeeRepositoryMySQL (JDBC puro, sin ORM).
 *
 * <p>Connection, PreparedStatement y ResultSet están mockeados; no se requiere base de datos real.
 */
@DisplayName("EmployeeRepositoryMySQL")
@ExtendWith(MockitoExtension.class)
class EmployeeRepositoryMySQLTest {

  @Mock private Connection connection;
  @Mock private PreparedStatement statement;
  @Mock private ResultSet resultSet;

  private EmployeeRepositoryMySQL repository;

  private static EmployeeModel buildEmployee() {
    return new EmployeeModel(
        new EmployeeId("1047382910"),
        new EmployeeFirstName("Laura"),
        new EmployeeLastName("Gomez Perez"),
        new EmployeePhone("3001234567"),
        new EmployeeAddress("Calle 10 # 5-20"),
        new EmployeeBankAccount("12345678901"),
        EmployeeStatus.ACTIVE);
  }

  private void stubRowValues() throws SQLException {
    when(resultSet.getString("id")).thenReturn("1047382910");
    when(resultSet.getString("first_name")).thenReturn("Laura");
    when(resultSet.getString("last_name")).thenReturn("Gomez Perez");
    when(resultSet.getString("phone")).thenReturn("3001234567");
    when(resultSet.getString("address")).thenReturn("Calle 10 # 5-20");
    when(resultSet.getString("bank_account")).thenReturn("12345678901");
    when(resultSet.getString("status")).thenReturn("ACTIVE");
    when(resultSet.getString("created_at")).thenReturn("2026-11-14 10:00:00");
    when(resultSet.getString("updated_at")).thenReturn("2026-11-14 10:00:00");
  }

  @BeforeEach
  void setUp() {
    repository = new EmployeeRepositoryMySQL(connection);
  }

  // ── getById

  @Test
  @DisplayName("getById() devuelve el empleado mapeado cuando existe")
  void getById_returnsEmployee_whenRowExists() throws SQLException {
    // Arrange
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    stubRowValues();

    // Act
    final Optional<EmployeeModel> result = repository.getById(new EmployeeId("1047382910"));

    // Assert
    assertAll(
        "mapeo de getById",
        () -> assertTrue(result.isPresent()),
        () -> assertEquals("Laura", result.get().getFirstName().value()),
        () -> assertEquals("12345678901", result.get().getBankAccount().value()),
        () -> assertEquals(EmployeeStatus.ACTIVE, result.get().getStatus()));
    verify(statement).setString(1, "1047382910");
  }

  @Test
  @DisplayName("getById() devuelve Optional vacio cuando no hay fila")
  void getById_returnsEmpty_whenNoRow() throws SQLException {
    // Arrange
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    // Act & Assert
    assertTrue(repository.getById(new EmployeeId("999")).isEmpty());
  }

  @Test
  @DisplayName("getById() lanza EmployeePersistenceException ante un SQLException")
  void getById_throwsPersistenceException_whenSqlFails() throws SQLException {
    // Arrange
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException("falla"));

    // Act & Assert
    assertThrows(
        EmployeePersistenceException.class, () -> repository.getById(new EmployeeId("1")));
  }

  // ── getAll

  @Test
  @DisplayName("getAll() devuelve todos los empleados mapeados")
  void getAll_returnsEmployees() throws SQLException {
    // Arrange
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true, false);
    stubRowValues();

    // Act
    final List<EmployeeModel> result = repository.getAll();

    // Assert
    assertEquals(1, result.size());
    assertEquals("1047382910", result.get(0).getId().value());
  }

  @Test
  @DisplayName("getAll() lanza EmployeePersistenceException ante un SQLException")
  void getAll_throwsPersistenceException_whenSqlFails() throws SQLException {
    // Arrange
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException("falla"));

    // Act & Assert
    assertThrows(EmployeePersistenceException.class, () -> repository.getAll());
  }

  // ── save

  @Test
  @DisplayName("save() inserta con los 7 parametros y devuelve el empleado persistido")
  void save_insertsAndReturnsPersistedEmployee() throws SQLException {
    // Arrange
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    stubRowValues();

    // Act
    final EmployeeModel result = repository.save(buildEmployee());

    // Assert
    assertEquals("1047382910", result.getId().value());
    verify(statement, atLeastOnce()).setString(1, "1047382910");
    verify(statement).setString(2, "Laura");
    verify(statement).setString(3, "Gomez Perez");
    verify(statement).setString(4, "3001234567");
    verify(statement).setString(5, "Calle 10 # 5-20");
    verify(statement).setString(6, "12345678901");
    verify(statement).setString(7, "ACTIVE");
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("save() lanza EmployeePersistenceException cuando el INSERT falla")
  void save_throwsPersistenceException_whenInsertFails() throws SQLException {
    // Arrange
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeUpdate()).thenThrow(new SQLException("duplicado"));

    // Act & Assert
    assertThrows(EmployeePersistenceException.class, () -> repository.save(buildEmployee()));
  }

  // ── update

  @Test
  @DisplayName("update() ejecuta el UPDATE y devuelve el empleado persistido")
  void update_updatesAndReturnsPersistedEmployee() throws SQLException {
    // Arrange
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    stubRowValues();

    // Act
    final EmployeeModel result = repository.update(buildEmployee());

    // Assert
    assertEquals("1047382910", result.getId().value());
    verify(statement).setString(7, "1047382910");
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("update() lanza EmployeePersistenceException cuando el UPDATE falla")
  void update_throwsPersistenceException_whenUpdateFails() throws SQLException {
    // Arrange
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeUpdate()).thenThrow(new SQLException("falla"));

    // Act & Assert
    assertThrows(EmployeePersistenceException.class, () -> repository.update(buildEmployee()));
  }

  // ── delete

  @Test
  @DisplayName("delete() ejecuta el DELETE con el id indicado")
  void delete_executesDeleteWithGivenId() throws SQLException {
    // Arrange
    when(connection.prepareStatement(anyString())).thenReturn(statement);

    // Act
    repository.delete(new EmployeeId("1047382910"));

    // Assert
    verify(statement).setString(1, "1047382910");
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("delete() lanza EmployeePersistenceException cuando el DELETE falla")
  void delete_throwsPersistenceException_whenDeleteFails() throws SQLException {
    // Arrange
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException("falla"));

    // Act & Assert
    assertThrows(
        EmployeePersistenceException.class, () -> repository.delete(new EmployeeId("1")));
  }
}
