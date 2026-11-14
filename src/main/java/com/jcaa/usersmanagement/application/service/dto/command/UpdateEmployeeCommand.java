package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateEmployeeCommand(
    @NotBlank(message = "el id no puede estar en blanco") String id,
    @NotBlank(message = "el nombre no puede estar en blanco")
        @Size(min = 2, message = "el nombre debe tener al menos 2 caracteres")
        String firstName,
    @NotBlank(message = "el apellido no puede estar en blanco")
        @Size(min = 2, message = "el apellido debe tener al menos 2 caracteres")
        String lastName,
    @NotBlank(message = "el teléfono no puede estar en blanco") String phone,
    @NotBlank(message = "la dirección no puede estar en blanco") String address,
    @NotBlank(message = "la cuenta bancaria no puede estar en blanco") String bankAccount,
    @NotBlank(message = "el estado no puede estar en blanco") String status)
{

}
