INSERT IGNORE INTO hogar (apellido_familia, direccion, telefono, sector, estado)
VALUES
    ('García Pérez', 'Sector A, Casa 1, Comunidad San Miguel', '50212345678', 'Sector A', 'ACTIVO'),
    ('López Méndez', 'Sector B, Casa 5, Comunidad San Miguel', '50287654321', 'Sector B', 'ACTIVO'),
    ('Martínez Ruiz', 'Sector A, Casa 3, Comunidad San Miguel', NULL, 'Sector A', 'MOROSO');

INSERT IGNORE INTO usuario (nombre, email, credenciales, rol, estado)
VALUES
    ('Comité AquaControl', 'comite@aquacontrol.com',
     '$2a$10$AruTFTM0GQGpq2zHGuib/eM9r2xvqYpPchE//XS/LlMuIp6Fiba4W',
     'COMITE', 'ACTIVO'),
    ('Técnico Fontanero', 'tecnico@aquacontrol.com',
     '$2a$10$fVmXZavm5mbAbF1vxNouh.Se2NfLD/ZCVP8mJA.e41uA7wfueHQEe',
     'TECNICO', 'ACTIVO');

INSERT IGNORE INTO usuario (nombre, email, credenciales, rol, estado, id_hogar)
VALUES
    ('Juan García', 'juan@ejemplo.com',
     '$2a$10$qH5drU2saQX685k1UTl.cOviTsK9SNC4g11wzypn7J8YgKarBOova',
     'REPRESENTANTE', 'ACTIVO', 1);

INSERT IGNORE INTO tanque (fecha, capacidad, nivel_actual, observacion)
VALUES (CURDATE(), 100000.00, 75000.00, 'Registro inicial del sistema');

INSERT IGNORE INTO aviso (titulo, descripcion, sector, vigencia)
VALUES ('Bienvenidos a AquaControl',
        'Sistema de gestión del agua de la Comunidad San Miguel ahora en funcionamiento.',
        NULL,
        DATE_ADD(CURDATE(), INTERVAL 30 DAY));