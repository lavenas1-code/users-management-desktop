package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateEmployeeCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmployeeCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmployeeCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmployeeByIdQuery;
import com.jcaa.usersmanagement.domain.enums.EmployeeStatus;
import com.jcaa.usersmanagement.domain.model.EmployeeModel;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeAddress;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeBankAccount;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeFirstName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeId;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeLastName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeePhone;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmployeeApplicationMapper {

  public EmployeeModel fromCreateCommandToModel(final CreateEmployeeCommand command) {
    return EmployeeModel.create(
        new EmployeeId(command.id()),
        new EmployeeFirstName(command.firstName()),
        new EmployeeLastName(command.lastName()),
        new EmployeePhone(command.phone()),
        new EmployeeAddress(command.address()),
        new EmployeeBankAccount(command.bankAccount()));
  }

  public EmployeeModel fromUpdateCommandToModel(final UpdateEmployeeCommand command) {
    return new EmployeeModel(
        new EmployeeId(command.id()),
        new EmployeeFirstName(command.firstName()),
        new EmployeeLastName(command.lastName()),
        new EmployeePhone(command.phone()),
        new EmployeeAddress(command.address()),
        new EmployeeBankAccount(command.bankAccount()),
        EmployeeStatus.fromString(command.status()));
  }

  public EmployeeId fromGetEmployeeByIdQueryToEmployeeId(final GetEmployeeByIdQuery query) {
    return new EmployeeId(query.id());
  }

  public EmployeeId fromDeleteCommandToEmployeeId(final DeleteEmployeeCommand command) {
    return new EmployeeId(command.id());
  }
}
