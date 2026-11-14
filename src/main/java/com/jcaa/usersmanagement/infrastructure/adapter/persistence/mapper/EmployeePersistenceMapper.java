package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import com.jcaa.usersmanagement.domain.enums.EmployeeStatus;
import com.jcaa.usersmanagement.domain.model.EmployeeModel;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeAddress;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeBankAccount;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeFirstName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeId;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeLastName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeePhone;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.EmployeePersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.EmployeeEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmployeePersistenceMapper {

  public EmployeePersistenceDto fromModelToDto(final EmployeeModel employee) {
    return new EmployeePersistenceDto(
        employee.getId().value(),
        employee.getFirstName().value(),
        employee.getLastName().value(),
        employee.getPhone().value(),
        employee.getAddress().value(),
        employee.getBankAccount().value(),
        employee.getStatus().name(),
        null,
        null);
  }

  public EmployeeEntity fromResultSetToEntity(final ResultSet resultSet) throws SQLException {
    return new EmployeeEntity(
        resultSet.getString("id"),
        resultSet.getString("first_name"),
        resultSet.getString("last_name"),
        resultSet.getString("phone"),
        resultSet.getString("address"),
        resultSet.getString("bank_account"),
        resultSet.getString("status"),
        resultSet.getString("created_at"),
        resultSet.getString("updated_at"));
  }

  public EmployeeModel fromEntityToModel(final EmployeeEntity entity) {
    return new EmployeeModel(
        new EmployeeId(entity.id()),
        new EmployeeFirstName(entity.firstName()),
        new EmployeeLastName(entity.lastName()),
        new EmployeePhone(entity.phone()),
        new EmployeeAddress(entity.address()),
        new EmployeeBankAccount(entity.bankAccount()),
        EmployeeStatus.fromString(entity.status()));
  }

  public EmployeeModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
    return fromEntityToModel(fromResultSetToEntity(resultSet));
  }

  public List<EmployeeModel> fromResultSetToModelList(final ResultSet resultSet)
      throws SQLException {
    final List<EmployeeModel> employees = new ArrayList<>();
    while (resultSet.next()) {
      employees.add(fromResultSetToModel(resultSet));
    }
    return employees;
  }
}
