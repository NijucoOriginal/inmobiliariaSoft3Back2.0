# Pruebas Unitarias del Sistema de Gestión de Usuarios

Este directorio contiene las pruebas unitarias y de integración para el sistema de gestión de usuarios del proyecto Inmobiliaria Eden. Aquí se valida el flujo real de negocio, desde la creación y activación de usuarios hasta la consulta y validación de existencia, asegurando que cada capa funcione como debe y que los errores se manejen correctamente.

## Estructura de las Pruebas

Las pruebas están organizadas siguiendo la estructura del proyecto:

```
src/test/java/com/jsebastian/eden/EdenSys/
├── controller/
│   └── UserControllerUnitTest.java      # Pruebas unitarias para el controlador de usuarios (sin MockMvc)
├── services/
│   └── UserServiceImplTest.java         # Pruebas unitarias para la lógica del servicio de usuarios
└── repository/
    └── UserRepositoryTest.java          # Pruebas de integración para el repositorio de usuarios
```

## Descripción de las Pruebas

### UserServiceImplTest.java

Pruebas unitarias para la lógica de negocio del servicio de usuarios. Aquí se simulan todos los escenarios relevantes del flujo real:

1. **crearUsuario_exitoso**
   - Simula la creación de un usuario cuando el email no existe previamente.
   - Verifica que la contraseña se codifique y que el usuario se cree con rol `PENDIENTE` (a la espera de activación por email).
   - Comprueba que el servicio retorna correctamente el DTO de respuesta.

2. **crearUsuario_emailDuplicado_lanzaExcepcion**
   - Simula el intento de crear un usuario con un email ya registrado.
   - Verifica que se lanza la excepción `ValueConflictException` y que no se guarda el usuario.

3. **crearUsuario_errorEnMapper_lanzaRuntimeException**
   - Simula un error inesperado en el mapeo de datos.
   - Verifica que se lanza una excepción genérica y que el mensaje es el esperado.

### UserControllerUnitTest.java

Pruebas unitarias para el controlador REST de usuarios, directamente sobre los métodos del controlador, sin levantar servidor ni contexto web:

1. **crearUsuario_exitoso**
   - Simula el flujo real de registro: el usuario envía sus datos y recibe una respuesta con status 200 y el rol `PENDIENTE`.
   - Verifica que el controlador retorna correctamente el DTO de respuesta.

2. **crearUsuario_emailDuplicado**
   - Simula el caso en que el email ya existe.
   - Verifica que el controlador responde con status 409 y el mensaje de conflicto adecuado.

3. **crearUsuario_errorInesperado**
   - Simula un error inesperado en el servicio.
   - Verifica que el controlador responde con status 500 y el mensaje de error interno.

4. **obtenerUsuarioPorId_exitoso**
   - Simula la consulta de un usuario existente por su ID.
   - Verifica que el controlador responde con status 200 y los datos correctos.

5. **obtenerUsuarioPorId_noEncontrado**
   - Simula la consulta de un usuario inexistente.
   - Verifica que el controlador responde con status 404.

### UserRepositoryTest.java

Pruebas de integración para el repositorio de usuarios, validando la interacción real con la base de datos:

1. **testFindByEmail**
   - Verifica que se puede encontrar un usuario por su email en la base de datos.

2. **testExistsByEmail**
   - Comprueba que el método `existsByEmail` retorna `true` cuando el email existe y `false` cuando no.

## Ejecución de las Pruebas

Para ejecutar las pruebas, puedes usar cualquiera de los siguientes comandos:

```bash
# Ejecutar todas las pruebas
y ./mvnw test

# Ejecutar pruebas específicas
./mvnw test -Dtest=UserServiceImplTest
./mvnw test -Dtest=UserControllerUnitTest

# Ejecutar una prueba específica
./mvnw test -Dtest=UserServiceImplTest#crearUsuario_exitoso
```

## Tecnologías Utilizadas

- JUnit 5
- Mockito
- Spring Boot Test
- AssertJ (para algunas aserciones)