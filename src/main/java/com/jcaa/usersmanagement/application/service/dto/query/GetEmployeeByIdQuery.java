package com.jcaa.usersmanagement.application.service.dto.query;

import jakarta.validation.constraints.NotBlank;

public record GetEmployeeByIdQuery(@NotBlank(message = "el id no puede estar en blanco") String id)
{

}
