# AquaControl — Backend v1.0.0
**Sistema de Gestión y Control del Agua para la Comunidad Rural "San Miguel"**
Universidad Mariano Gálvez de Guatemala — Proyecto III

---

## Stack Tecnológico
- **Java 17** + **Spring Boot 3.2.5**
- **Spring Security** + **JWT (JJWT 0.11.5)**
- **Spring Data JPA** + **Hibernate**
- **MySQL 8** (base de datos: `aquacontrol_db`)
- **Lombok**

---

## Configuración rápida

### 1. Base de Datos
```sql
-- Ejecutar el script SQL incluido:
mysql -u root -p < src/main/resources/aquacontrol_db.sql
```

### 2. Variables de entorno
Editar `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/aquacontrol_db?useSSL=false&serverTimezone=America/Guatemala&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

### 3. Ejecutar
```bash
mvn spring-boot:run
# El servidor inicia en http://localhost:8080
```

---

## Credenciales de prueba (seed)
| Rol | Email | Password |
|-----|-------|----------|
| Comité | comite@aquacontrol.com | Admin1234! |
| Técnico | tecnico@aquacontrol.com | Tecnico1234! |
| Representante | juan@ejemplo.com | Casa1234! |

> ⚠️ Los hashes del seed usan BCrypt. Si no coinciden, crear usuarios vía POST /api/usuarios después de iniciar sesión como Comité.

---

## Endpoints de la API

### Autenticación (público)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/login` | Iniciar sesión → retorna token JWT |

**Body:**
```json
{ "email": "comite@aquacontrol.com", "password": "Admin1234!" }
```

**Todos los demás endpoints requieren header:**
```
Authorization: Bearer <token>
```

---

### Hogares `/api/hogares`
| Método | Endpoint | Roles | Descripción |
|--------|----------|-------|-------------|
| GET | `/api/hogares?page=0&size=10` | COMITE/TECNICO/REPRESENTANTE | Listar (paginado) |
| GET | `/api/hogares/{id}` | COMITE/TECNICO/REPRESENTANTE | Obtener por ID |
| GET | `/api/hogares/sector/{sector}` | COMITE/TECNICO/REPRESENTANTE | Filtrar por sector |
| POST | `/api/hogares` | COMITE | Registrar nuevo hogar |
| PUT | `/api/hogares/{id}` | COMITE | Actualizar hogar |
| PATCH | `/api/hogares/{id}/estado?estado=MOROSO` | COMITE | Cambiar estado |
| DELETE | `/api/hogares/{id}` | COMITE | Eliminar (bloqueado si tiene aportes) |

---

### Aportes `/api/aportes`
| Método | Endpoint | Roles | Descripción |
|--------|----------|-------|-------------|
| POST | `/api/aportes` | COMITE | Registrar aporte mensual |
| GET | `/api/aportes/{idHogar}?page=0&size=10` | COMITE/REPRESENTANTE | Listar aportes del hogar |
| GET | `/api/aportes/{idHogar}/historial?inicio=2026-01-01&fin=2026-12-31` | COMITE/REPRESENTANTE | Historial por fecha |

---

### Problemas `/api/problemas`
| Método | Endpoint | Roles | Descripción |
|--------|----------|-------|-------------|
| GET | `/api/problemas?page=0&size=10` | COMITE/TECNICO | Listar todos |
| GET | `/api/problemas/{id}` | COMITE/TECNICO | Obtener por ID |
| GET | `/api/problemas/hogar/{idHogar}` | COMITE/TECNICO/REPRESENTANTE | Por hogar |
| POST | `/api/problemas` | COMITE/REPRESENTANTE | Registrar problema |
| PUT | `/api/problemas/{id}/estado` | COMITE/TECNICO | Actualizar estado |

---

### Mantenimiento `/api/mantenimiento`
| Método | Endpoint | Roles | Descripción |
|--------|----------|-------|-------------|
| GET | `/api/mantenimiento/problema/{idProblema}` | COMITE/TECNICO | Por problema |
| GET | `/api/mantenimiento/{id}` | COMITE/TECNICO | Por ID |
| POST | `/api/mantenimiento` | COMITE/TECNICO | Registrar (físico o administrativo) |
| PATCH | `/api/mantenimiento/{id}/estado` | COMITE/TECNICO | Cambiar estado |

---

### Distribución `/api/distribucion`
| Método | Endpoint | Roles | Descripción |
|--------|----------|-------|-------------|
| GET | `/api/distribucion` | COMITE/REPRESENTANTE | Listar todas |
| GET | `/api/distribucion/{sector}` | COMITE/REPRESENTANTE | Por sector |
| POST | `/api/distribucion` | COMITE | Programar distribución |
| PATCH | `/api/distribucion/{id}/estado` | COMITE | Actualizar estado |

---

### Tanque `/api/tanque`
| Método | Endpoint | Roles | Descripción |
|--------|----------|-------|-------------|
| GET | `/api/tanque` | COMITE/TECNICO | Historial de niveles |
| GET | `/api/tanque/ultimo` | COMITE/TECNICO/REPRESENTANTE | Último nivel registrado |
| GET | `/api/tanque/{id}` | COMITE/TECNICO | Por ID |
| POST | `/api/tanque` | COMITE | Registrar nivel manual |

---

### Avisos `/api/avisos`
| Método | Endpoint | Roles | Descripción |
|--------|----------|-------|-------------|
| GET | `/api/avisos` | COMITE/REPRESENTANTE | Avisos vigentes |
| GET | `/api/avisos/sector/{sector}` | COMITE/REPRESENTANTE | Por sector |
| GET | `/api/avisos/todos` | COMITE | Todos (incluyendo vencidos) |
| POST | `/api/avisos` | COMITE | Publicar aviso |
| DELETE | `/api/avisos/{id}` | COMITE | Eliminar aviso |

---

### Usuarios `/api/usuarios`
| Método | Endpoint | Roles | Descripción |
|--------|----------|-------|-------------|
| GET | `/api/usuarios` | COMITE | Listar usuarios |
| GET | `/api/usuarios/{id}` | COMITE | Por ID |
| POST | `/api/usuarios` | COMITE | Crear usuario |
| PUT | `/api/usuarios/{id}` | COMITE | Actualizar |
| PATCH | `/api/usuarios/{id}/estado` | COMITE | Activar/Desactivar |
| PATCH | `/api/usuarios/{id}/password` | COMITE | Cambiar contraseña |

---

### Reportes `/api/reportes`
| Método | Endpoint | Roles | Descripción |
|--------|----------|-------|-------------|
| GET | `/api/reportes/resumen` | COMITE | Dashboard general |
| GET | `/api/reportes/aportes` | COMITE | Resumen de pagos |
| GET | `/api/reportes/mantenimiento` | COMITE | Historial de mantenimientos |

---

## Reglas de negocio implementadas
1. **Cuota fija mensual**: sin pagos parciales (validación en AporteService)
2. **Morosidad automática**: 2+ aportes PENDIENTE → estado MOROSO
3. **ON DELETE RESTRICT**: no se puede eliminar hogar con aportes registrados
4. **Nivel crítico del tanque**: alerta cuando nivel ≤ 10% de capacidad
5. **Control de acceso por roles**: COMITE / TECNICO / REPRESENTANTE
6. **Sesiones Stateless**: JWT con expiración de 8 horas (28800000 ms)
7. **Paginación obligatoria**: hogares y aportes paginados (15KB máx por request)

---

## Estructura del proyecto
```
src/main/java/com/aquacontrol/
├── AquaControlApplication.java
├── controller/          # REST Controllers (8 controladores)
├── service/             # Lógica de negocio (8 servicios)
├── repository/          # Spring Data JPA (8 repositorios)
├── entity/              # Entidades JPA (7 entidades)
├── dto/                 # Data Transfer Objects
├── security/            # JWT + Spring Security
└── exception/           # Manejo global de errores
```
