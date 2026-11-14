package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record CreateEmployeeRequest(
    String id,
    String firstName,
    String lastName,
    String phone,
    String address,
    String bankAccount) {}
