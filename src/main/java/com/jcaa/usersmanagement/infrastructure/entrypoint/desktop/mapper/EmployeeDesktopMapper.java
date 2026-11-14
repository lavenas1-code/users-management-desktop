package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateEmployeeCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmployeeCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmployeeCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmployeeByIdQuery;
import com.jcaa.usersmanagement.domain.model.EmployeeModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateEmployeeRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.EmployeeResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateEmployeeRequest;
import java.util.List;

public final class EmployeeDesktopMapper {

  private EmployeeDesktopMapper() {}

  public static CreateEmployeeCommand toCreateCommand(final CreateEmployeeRequest request) {
    return new CreateEmployeeCommand(
        request.id(),
        request.firstName(),
        request.lastName(),
        request.phone(),
        request.address(),
        request.bankAccount());
  }

  public static UpdateEmployeeCommand toUpdateCommand(final UpdateEmployeeRequest request) {
    return new UpdateEmployeeCommand(
        request.id(),
        request.firstName(),
        request.lastName(),
        request.phone(),
        request.address(),
        request.bankAccount(),
        request.status());
  }

  public static DeleteEmployeeCommand toDeleteCommand(final String id) {
    return new DeleteEmployeeCommand(id);
  }

  public static GetEmployeeByIdQuery toGetByIdQuery(final String id) {
    return new GetEmployeeByIdQuery(id);
  }

  public static EmployeeResponse toResponse(final EmployeeModel employee) {
    return new EmployeeResponse(
        employee.getId().value(),
        employee.getFirstName().value(),
        employee.getLastName().value(),
        employee.getPhone().value(),
        employee.getAddress().value(),
        employee.getBankAccount().value(),
        employee.getStatus().name());
  }

  public static List<EmployeeResponse> toResponseList(final List<EmployeeModel> employees) {
    return employees.stream().map(EmployeeDesktopMapper::toResponse).toList();
  }
}
