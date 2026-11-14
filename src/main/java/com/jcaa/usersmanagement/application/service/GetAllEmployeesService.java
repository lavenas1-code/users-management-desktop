package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllEmployeesUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllEmployeesPort;
import com.jcaa.usersmanagement.domain.model.EmployeeModel;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GetAllEmployeesService implements GetAllEmployeesUseCase {

  private final GetAllEmployeesPort getAllEmployeesPort;

  @Override
  public List<EmployeeModel> execute() {
    return getAllEmployeesPort.getAll();
  }
}
