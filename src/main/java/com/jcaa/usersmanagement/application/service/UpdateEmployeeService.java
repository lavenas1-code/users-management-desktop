package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateEmployeeUseCase;
import com.jcaa.usersmanagement.application.port.out.GetEmployeeByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateEmployeePort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmployeeCommand;
import com.jcaa.usersmanagement.application.service.mapper.EmployeeApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.EmployeeNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmployeeModel;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateEmployeeService implements UpdateEmployeeUseCase {

  private final UpdateEmployeePort updateEmployeePort;
  private final GetEmployeeByIdPort getEmployeeByIdPort;
  private final Validator validator;

  @Override
  public EmployeeModel execute(final UpdateEmployeeCommand command) {
    validateCommand(command);

    final EmployeeId employeeId = new EmployeeId(command.id());
    ensureEmployeeExists(employeeId);

    final EmployeeModel employeeToUpdate =
        EmployeeApplicationMapper.fromUpdateCommandToModel(command);
    return updateEmployeePort.update(employeeToUpdate);
  }

  private void validateCommand(final UpdateEmployeeCommand command) {
    final Set<ConstraintViolation<UpdateEmployeeCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureEmployeeExists(final EmployeeId employeeId) {
    getEmployeeByIdPort
        .getById(employeeId)
        .orElseThrow(() -> EmployeeNotFoundException.becauseIdWasNotFound(employeeId.value()));
  }
}
