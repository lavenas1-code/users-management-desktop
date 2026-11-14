package com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto;

public record EmployeePersistenceDto(
    String id,
    String firstName,
    String lastName,
    String phone,
    String address,
    String bankAccount,
    String status,
    String createdAt,
    String updatedAt) {}
