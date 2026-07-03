# AGENTS.md — Billetera API

## Stack tecnológico

| Componente | Versión |
|---|---|
| Java | 17.0.19 (Eclipse Adoptium) |
| Spring Boot | 4.1.0 |
| Spring Cloud | 2025.1.2 (Oakwood) |
| OpenFeign | 5.0.2 |
| MySQL | 8.0.43 |
| Hibernate | 7.4.1.Final |
| Lombok | 1.18.46 |
| Maven | Wrapper (mvnw.cmd) |

---

## Arquitectura

Layered architecture estándar:

```
billetera/
├── BilleteraApplication.java        # @SpringBootApplication + @EnableFeignClients
├── constants/
│   └── ApiConstants.java            # BASIC_AUTH_PREFIX, BEARER_PREFIX, timeouts
├── config/
│   └── ApiConstants.java            # DUPLICADO — mismo contenido que constants/
├── controller/
│   ├── ClienteController.java       # /api/v1/clientes/*
│   └── BilleteraController.java     # /api/v1/billetera/*
├── dto/
│   ├── ClienteRequestDTO.java       # documento, nombres, email, celular
│   ├── RecargaRequestDTO.java       # documento, celular, valor
│   ├── IniciarPagoRequestDTO.java   # documento, celular, valor
│   ├── IniciarPagoResponseDTO.java  # sessionId, mensaje
│   ├── ConfirmarPagoRequestDTO.java # sessionId, token
│   ├── ConsultarSaldoRequestDTO.java # documento, celular
│   ├── ConsultarSaldoResponseDTO.java # saldo, nombre
│   ├── MessageResponseDTO.java      # message
│   ├── EmailRequestDTO.java         # from, to, subject, text
│   ├── EmailResponseDTO.java        # id, message
│   └── StandardResponse.java        # success, cod_error, message_error, data
├── entity/
│   ├── Cliente.java                 # id, documento, nombres, email, celular
│   ├── Billetera.java               # id, cliente (OneToOne), saldo (BigDecimal)
│   └── TransaccionPendiente.java    # id, sessionId, token, valor, cliente, expiracion, estado
├── enums/
│   └── EstadoTransaccion.java       # PENDIENTE, COMPLETADA, EXPIRADA
├── exception/
│   ├── SaldoInsuficienteException.java
│   └── GlobalExceptionHandler.java  # @RestControllerAdvice
├── intercom/
│   └── MailgunIntercom.java         # @FeignClient para Mailgun API
├── repository/
│   ├── ClienteRepository.java
│   ├── BilleteraRepository.java
│   └── TransaccionPendienteRepository.java
└── service/
    ├── ClienteService.java
    ├── BilleteraService.java
    └── EmailService.java
```

---

## API Endpoints

Base URL: `http://localhost:9092/api/v1`

### Clientes

#### `POST /clientes/registrar` — Registrar cliente + crear billetera con saldo 0

```json
{
  "documento": "1234567890",
  "nombres": "Frank Sánchez",
  "email": "fsanchezr2002@gmail.com",
  "celular": "3101234567"
}
```

**Response éxito**: `{ "success": true, "cod_error": "00", "message_error": "Cliente registrado exitosamente", "data": { ... } }`

### Billetera

#### `POST /billetera/recargar` — Recargar saldo

```json
{
  "documento": "1234567890",
  "celular": "3101234567",
  "valor": 50000
}
```

#### `GET /billetera` — Consultar saldo (usa body con GET)

```json
{
  "documento": "1234567890",
  "celular": "3101234567"
}
```

#### `POST /billetera/pago` — Iniciar pago (envía token por email)

```json
{
  "documento": "1234567890",
  "celular": "3101234567",
  "valor": 10000
}
```

Devuelve `sessionId` para usar en confirmación.

#### `POST /billetera/confirmar` — Confirmar pago con token

```json
{
  "sessionId": "uuid-del-paso-anterior",
  "token": "197699"
}
```

---

## Códigos de error

| Código | Significado |
|--------|-------------|
| `00` | Operación exitosa |
| `01` | Cliente no registrado / ya registrado |
| `02` | Billetera inexistente / error de recarga |
| `03` | Saldo insuficiente |
| `04` | Token incorrecto / email no enviado |
| `05` | Billetera no encontrada en confirmación |
| `99` | Error interno del servidor |

---

## Formato de respuesta estándar

```json
{
  "success": true,
  "cod_error": "00",
  "message_error": "Operación exitosa",
  "data": { ... }
}
```

---

## Integración con Mailgun (OpenFeign)

**FeignClient**: `MailgunIntercom` — llama a `https://api.mailgun.net/v3/{domain}/messages`

**Autenticación**: Basic Auth con `api:{apiKey}` en Base64.

**Config** (application.yaml):
```yaml
mailgun:
  base-url: https://api.mailgun.net/v3
  domain: sandboxe06237698f8f4965aac48d8a78398093.mailgun.org
  from: noreply@...
  api-key: ${MAILGUN_API_KEY:default_value}

feign:
  client:
    config:
      mailgun-client:
        connect-timeout: 5000
        read-timeout: 10000
```

**Sandbox**: solo hasta 5 destinatarios autorizados. Cada destinatario debe verificarse haciendo clic en un link enviado por Mailgun.

---

## Infraestructura y base de datos

| Propiedad | Valor |
|---|---|
| DB | MySQL 8.0 en localhost:3306 |
| DB Name | `billetera` (se crea automáticamente) |
| User | `root` |
| Password | `12345` |
| DDL | `update` (Hibernate crea/actualiza tablas) |
| Server port | 9092 |
| Context path | `/api/v1/` |

---

## Cómo ejecutar

```powershell
# En la raíz del proyecto:
$env:MAILGUN_API_KEY="<tu-api-key>"
mvnw.cmd spring-boot:run
```

**Requisitos**:
- JDK 17 (seteado en línea 2 de `mvnw.cmd`)
- MySQL corriendo en localhost:3306
- Variable de entorno `MAILGUN_API_KEY` o el valor por defecto en application.yaml

---

## Notas técnicas importantes

1. **Spring Boot 4.1.0 + Spring Cloud**: Usar Spring Cloud 2025.1.2 (Oakwood). NO usar 2023.0.0 (Leyton) — es incompatible.
2. **RestTemplate eliminado**: Migrado a OpenFeign declarativo.
3. **Duplicado**: Existe `config/ApiConstants.java` duplicado de `constants/ApiConstants.java` — limpiar.
4. **Bug conocido**: `StandardResponse.SaldoInsuficiente()` devuelve `success: true` en vez de `false`.
5. **GET con body**: `GET /billetera` usa `@RequestBody` — no es estándar REST.
6. **JWT**: Configurado `security.jwt.secret-key` en YAML pero no hay filtro de seguridad implementado.
7. **Mailgun sandbox**: Para enviar a un destinatario real hay que agregarlo en Mailgun → Domains → Authorized Recipients y verificar el email.
