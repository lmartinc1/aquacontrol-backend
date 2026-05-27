-- ============================================================
-- AquaControl - Script de Creación de Base de Datos
-- Sistema de Gestión y Control del Agua - Comunidad San Miguel
-- Universidad Mariano Gálvez de Guatemala
-- Proyecto III - v1.0.0
-- ============================================================

CREATE DATABASE IF NOT EXISTS aquacontrol_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE aquacontrol_db;

-- ------------------------------------------------------------
-- TABLA: hogar
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS hogar (
    id_hogar        INT AUTO_INCREMENT PRIMARY KEY,
    apellido_familia VARCHAR(100) NOT NULL,
    direccion        VARCHAR(200) NOT NULL,
    telefono         VARCHAR(20),
    sector           VARCHAR(50),
    estado           ENUM('ACTIVO','MOROSO','SUSPENDIDO','INACTIVO') NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- TABLA: usuario
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario  INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    credenciales VARCHAR(255) NOT NULL,
    rol         ENUM('COMITE','TECNICO','REPRESENTANTE') NOT NULL,
    estado      ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    id_hogar    INT,
    CONSTRAINT fk_usuario_hogar FOREIGN KEY (id_hogar)
        REFERENCES hogar(id_hogar) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- TABLA: tanque
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS tanque (
    id_tanque   INT AUTO_INCREMENT PRIMARY KEY,
    fecha       DATE NOT NULL,
    capacidad   DECIMAL(10,2) NOT NULL,
    nivel_actual DECIMAL(10,2) NOT NULL,
    observacion TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- TABLA: distribucion
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS distribucion (
    id_distribucion INT AUTO_INCREMENT PRIMARY KEY,
    id_tanque       INT NOT NULL,
    sector          VARCHAR(50) NOT NULL,
    dia             DATE NOT NULL,
    hora            TIME NOT NULL,
    observacion     TEXT,
    estado          ENUM('PROGRAMADO','COMPLETADO','CANCELADO') NOT NULL DEFAULT 'PROGRAMADO',
    CONSTRAINT fk_distribucion_tanque FOREIGN KEY (id_tanque)
        REFERENCES tanque(id_tanque) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- TABLA: aporte
-- ON DELETE RESTRICT protege el histórico financiero
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS aporte (
    id_pago             INT AUTO_INCREMENT PRIMARY KEY,
    id_hogar            INT NOT NULL,
    fecha               DATE NOT NULL,
    monto               DECIMAL(10,2) NOT NULL,
    estado              ENUM('PAGADO','PENDIENTE','ANULADO') NOT NULL DEFAULT 'PAGADO',
    mes_correspondiente  VARCHAR(20),
    anio_correspondiente INT,
    CONSTRAINT fk_aporte_hogar FOREIGN KEY (id_hogar)
        REFERENCES hogar(id_hogar) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- TABLA: problema
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS problema (
    id_problema INT AUTO_INCREMENT PRIMARY KEY,
    id_hogar    INT NOT NULL,
    descripcion TEXT NOT NULL,
    fecha       DATE NOT NULL,
    estado      ENUM('PENDIENTE','EN_PROCESO','RESUELTO','CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
    tipo        VARCHAR(50),
    CONSTRAINT fk_problema_hogar FOREIGN KEY (id_hogar)
        REFERENCES hogar(id_hogar) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- TABLA: mantenimiento
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS mantenimiento (
    id_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_problema      INT NOT NULL,
    fecha            DATE NOT NULL,
    descripcion      TEXT,
    estado           ENUM('PENDIENTE','EN_PROCESO','COMPLETADO','CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
    tipo_actividad   ENUM('TECNICO','ADMINISTRATIVO') NOT NULL DEFAULT 'TECNICO',
    CONSTRAINT fk_mantenimiento_problema FOREIGN KEY (id_problema)
        REFERENCES problema(id_problema) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- TABLA: aviso
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS aviso (
    id_aviso    INT AUTO_INCREMENT PRIMARY KEY,
    titulo      VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    sector      VARCHAR(50),          -- NULL = aviso general para todos
    vigencia    DATE NOT NULL,
    id_hogar    INT,
    CONSTRAINT fk_aviso_hogar FOREIGN KEY (id_hogar)
        REFERENCES hogar(id_hogar) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- DATOS INICIALES (SEED)
-- ============================================================

-- Hogar de prueba
INSERT INTO hogar (apellido_familia, direccion, telefono, sector, estado)
VALUES
    ('García Pérez', 'Sector A, Casa 1, Comunidad San Miguel', '50212345678', 'Sector A', 'ACTIVO'),
    ('López Méndez', 'Sector B, Casa 5, Comunidad San Miguel', '50287654321', 'Sector B', 'ACTIVO'),
    ('Martínez Ruiz', 'Sector A, Casa 3, Comunidad San Miguel', NULL, 'Sector A', 'MOROSO');

-- Usuario Comité (password: Admin1234!)
-- Hash BCrypt generado: $2a$10$ejemplo_hash_comite (debe generarse con la app)
INSERT INTO usuario (nombre, email, credenciales, rol, estado)
VALUES
    ('Comité AquaControl', 'comite@aquacontrol.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVNugQ55dG',
     'COMITE', 'ACTIVO');

-- Usuario Técnico (password: Tecnico1234!)
INSERT INTO usuario (nombre, email, credenciales, rol, estado)
VALUES
    ('Técnico Fontanero', 'tecnico@aquacontrol.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVNugQ55dG',
     'TECNICO', 'ACTIVO');

-- Usuario Representante (password: Casa1234!)
INSERT INTO usuario (nombre, email, credenciales, rol, estado, id_hogar)
VALUES
    ('Juan García', 'juan@ejemplo.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVNugQ55dG',
     'REPRESENTANTE', 'ACTIVO', 1);

-- Registro inicial del tanque (100,000 galones, 75% lleno)
INSERT INTO tanque (fecha, capacidad, nivel_actual, observacion)
VALUES (CURDATE(), 100000.00, 75000.00, 'Registro inicial del sistema');

-- Aviso general de bienvenida
INSERT INTO aviso (titulo, descripcion, sector, vigencia)
VALUES ('Bienvenidos a AquaControl',
        'Sistema de gestión del agua de la Comunidad San Miguel ahora en funcionamiento.',
        NULL,
        DATE_ADD(CURDATE(), INTERVAL 30 DAY));
