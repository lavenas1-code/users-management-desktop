-- =============================================
-- Script de creación de la base de datos
-- Gestión de Usuarios - Arquitectura Hexagonal
-- =============================================

CREATE DATABASE IF NOT EXISTS crud_usuarios
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE crud_usuarios;

CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        ENUM('ADMIN', 'MEMBER', 'REVIEWER') NOT NULL,
    status      ENUM('ACTIVE', 'INACTIVE', 'PENDING', 'BLOCKED') NOT NULL DEFAULT 'PENDING',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Usuario administrador inicial (password: Admin1234!)
INSERT INTO users (id, name, email, password, role, status)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'Administrador',
    'admin@example.com',
    '$2a$12$placeholderHashReplaceWithRealBCryptHash',
    'ADMIN',
    'ACTIVE'
);

-- ---------------------------------------------------------------------
-- Tabla: employees  (entidad nueva - caso de estudio Nomina)
--   id            : DNI / documento del empleado
--   bank_account  : cuenta para transferir la nomina (solo digitos)
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS employees;
CREATE TABLE employees (
                           id            VARCHAR(20)  NOT NULL PRIMARY KEY,
                           first_name    VARCHAR(100) NOT NULL,
                           last_name     VARCHAR(100) NOT NULL,
                           phone         VARCHAR(16)  NOT NULL,
                           address       VARCHAR(255) NOT NULL,
                           bank_account  VARCHAR(20)  NOT NULL UNIQUE,
                           status        ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
                           created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

LOCK TABLES employees WRITE;
INSERT INTO employees (id, first_name, last_name, phone, address, bank_account, status) VALUES
                                                                                            ('1047382910', 'Laura',  'Gomez Perez',   '3001234567', 'Calle 10 # 5-20, Cartagena',    '12345678901', 'ACTIVE'),
                                                                                            ('1098765432', 'Carlos', 'Mendoza Ruiz',  '3109876543', 'Carrera 7 # 12-30, Cartagena',  '98765432109', 'ACTIVE'),
                                                                                            ('1123456789', 'Marta',  'Torres Ortega', '3205551122', 'Av. Pedro de Heredia # 45-10',  '55512345678', 'INACTIVE');
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

