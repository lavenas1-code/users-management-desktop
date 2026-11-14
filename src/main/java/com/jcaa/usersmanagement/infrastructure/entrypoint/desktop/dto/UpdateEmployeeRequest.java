package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record UpdateEmployeeRequest(
    String id,
    String firstName,
    String lastName,
    String phone,
    String address,
    String bankAccount,
    String status) {}
