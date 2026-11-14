package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetEmployeeByIdQuery;
import com.jcaa.usersmanagement.domain.model.EmployeeModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GetEmployeeByIdUseCase {
  EmployeeModel execute(@NotNull @Valid GetEmployeeByIdQuery query);
}
