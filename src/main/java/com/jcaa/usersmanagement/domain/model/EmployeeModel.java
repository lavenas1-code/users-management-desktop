package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.EmployeeStatus;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeAddress;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeBankAccount;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeFirstName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeId;
import com.jcaa.usersmanagement.domain.valueobject.EmployeeLastName;
import com.jcaa.usersmanagement.domain.valueobject.EmployeePhone;
import lombok.Value;

@Value
public class EmployeeModel {

  EmployeeId id;
  EmployeeFirstName firstName;
  EmployeeLastName lastName;
  EmployeePhone phone;
  EmployeeAddress address;
  EmployeeBankAccount bankAccount;
  EmployeeStatus status;

  public static EmployeeModel create(
      final EmployeeId id,
      final EmployeeFirstName firstName,
      final EmployeeLastName lastName,
      final EmployeePhone phone,
      final EmployeeAddress address,
      final EmployeeBankAccount bankAccount) {
    return new EmployeeModel(
        id, firstName, lastName, phone, address, bankAccount, EmployeeStatus.ACTIVE);
  }

  public EmployeeModel activate() {
    return new EmployeeModel(
        id, firstName, lastName, phone, address, bankAccount, EmployeeStatus.ACTIVE);
  }

  public EmployeeModel deactivate() {
    return new EmployeeModel(
        id, firstName, lastName, phone, address, bankAccount, EmployeeStatus.INACTIVE);
  }
}
