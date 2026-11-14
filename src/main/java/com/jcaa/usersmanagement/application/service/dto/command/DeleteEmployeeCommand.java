package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;

public record DeleteEmployeeCommand(
    @NotBlank(message = "el id no puede estar en blanco") String id
) {

}
