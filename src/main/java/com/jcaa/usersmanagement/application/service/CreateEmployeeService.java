package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateEmployeeUseCase;
import com.jcaa.usersmanagement.application.port.out.GetEmployeeByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveEmployeePort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateEmployeeCommand;
import com.jcaa.usersmanagement.application.service.mapper.EmployeeApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.EmployeeAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.EmployeeModel;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateEmployeeService implements CreateEmployeeUseCase {

  private final SaveEmployeePort saveEmployeePort;
  private final GetEmployeeByIdPort getEmployeeByIdPort;
  private final Validator validator;

  @Override
  public EmployeeModel execute(final CreateEmployeeCommand command) {
    validateCommand(command);

    final EmployeeId employeeId = new EmployeeId(command.id());
    ensureIdIsNotTaken(employeeId);

    final EmployeeModel employeeToSave = EmployeeApplicationMapper.fromCreateCommandToModel(command);
    return saveEmployeePort.save(employeeToSave);
  }

  private void validateCommand(final CreateEmployeeCommand command) {
    final Set<ConstraintViolation<CreateEmployeeCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureIdIsNotTaken(final EmployeeId employeeId) {
    getEmployeeByIdPort
        .getById(employeeId)
        .ifPresent(
            ignored -> {
              throw EmployeeAlreadyExistsException.becauseIdAlreadyExists(employeeId.value());
            });
  }
}
