# Pruebas Unitarias del Sistema de Gestión de Usuarios e Inmuebles

Este directorio contiene las pruebas unitarias y de integración para el sistema de gestión de usuarios e inmuebles del proyecto Inmobiliaria Eden. Aquí se valida el flujo real de negocio, desde la creación y activación de usuarios hasta la consulta y validación de existencia, asegurando que cada capa funcione como debe y que los errores se manejen correctamente.

## Estructura de las Pruebas

Las pruebas están organizadas siguiendo la estructura del proyecto:

```
src/test/java/com/jsebastian/eden/EdenSys/
├── controller/
│   ├── UserControllerUnitTest.java       # Pruebas unitarias para el controlador de usuarios (sin MockMvc)
│   └── InmuebleControllerUnitTest.java   # Pruebas unitarias para el controlador de inmuebles (sin MockMvc)
├── services/
│   ├── UserServiceImplTest.java          # Pruebas unitarias para la lógica del servicio de usuarios
│   └── InmuebleServiceImplTest.java      # Pruebas unitarias para la lógica del servicio de inmuebles
└── repository/
    ├── UserRepositoryTest.java           # Pruebas de integración para el repositorio de usuarios
    ├── InmuebleRepositoryTest.java       # Pruebas de integración para el repositorio de inmuebles
    ├── UserRepositorySimpleTest.java     # Pruebas de integración simplificadas para usuarios
    └── InmuebleRepositorySimpleTest.java # Pruebas de integración simplificadas para inmuebles
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

### InmuebleServiceImplTest.java

Pruebas unitarias para la lógica de negocio del servicio de inmuebles:

1. **crearInmueble_exitoso**
   - Simula la creación de un inmueble cuando el usuario es válido y tiene rol CLIENTE.
   - Verifica que el inmueble se cree correctamente con estado PENDIENTE.
   - Comprueba que el servicio retorna correctamente el DTO de respuesta.

2. **crearInmueble_usuarioNoEncontrado_lanzaExcepcion**
   - Simula el intento de crear un inmueble cuando el usuario no existe.
   - Verifica que se lanza una excepción y que no se guarda el inmueble.

3. **crearInmueble_usuarioNoEsCliente_lanzaExcepcion**
   - Simula el intento de crear un inmueble cuando el usuario no tiene rol CLIENTE.
   - Verifica que se lanza una excepción y que no se guarda el inmueble.

4. **obtenerInmueble_exitoso**
   - Simula la consulta de un inmueble existente por su ID.
   - Verifica que el servicio retorna correctamente el DTO de respuesta.

5. **obtenerInmueble_noEncontrado_lanzaExcepcion**
   - Simula la consulta de un inmueble inexistente.
   - Verifica que se lanza una excepción.

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

### InmuebleControllerUnitTest.java

Pruebas unitarias para el controlador REST de inmuebles:

1. **crearInmueble_exitoso**
   - Simula el flujo real de creación de inmueble.
   - Verifica que el controlador retorna correctamente el DTO de respuesta con status 201.

2. **crearInmueble_error_lanzaExcepcion**
   - Simula un error en la creación del inmueble.
   - Verifica que el controlador responde con status 409 y el mensaje de error adecuado.

3. **obtenerInmueble_exitoso**
   - Simula la consulta de un inmueble existente por su ID.
   - Verifica que el controlador responde con status 200 y los datos correctos.

4. **obtenerInmueble_noEncontrado**
   - Simula la consulta de un inmueble inexistente.
   - Verifica que el controlador responde con status 404.

5. **obtenerListaDeInmuebles_exitoso**
   - Simula la consulta de la lista de todos los inmuebles.
   - Verifica que el controlador responde con status 200 y la lista de inmuebles.

### UserRepositoryTest.java

Pruebas de integración para el repositorio de usuarios, validando la interacción real con la base de datos:

1. **testFindByEmail**
   - Verifica que se puede encontrar un usuario por su email en la base de datos.

2. **testExistsByEmail**
   - Comprueba que el método `existsByEmail` retorna `true` cuando el email existe y `false` cuando no.

### InmuebleRepositoryTest.java

Pruebas de integración para el repositorio de inmuebles:

1. **findByCodigoInmueble_DebeRetornarInmueble_CuandoExiste**
   - Verifica que se puede encontrar un inmueble por su código en la base de datos.

2. **existsByCodigoInmueble_DebeRetornarTrue_CuandoInmuebleExiste**
   - Comprueba que el método `existsByCodigoInmueble` retorna `true` cuando el código existe.

3. **existsByCodigoInmueble_DebeRetornarFalse_CuandoInmuebleNoExiste**
   - Comprueba que el método `existsByCodigoInmueble` retorna `false` cuando el código no existe.

4. **findByCiudad_DebeRetornarInmueblesConCiudadEspecificado**
   - Verifica que se pueden encontrar inmuebles por ciudad.

5. **findByTipoNegocio_DebeRetornarInmueblesConTipoNegocioEspecificado**
   - Verifica que se pueden encontrar inmuebles por tipo de negocio.

## Ejecución de las Pruebas

Para ejecutar las pruebas, puedes usar cualquiera de los siguientes comandos:

```bash
# Ejecutar todas las pruebas
./mvnw test

# Ejecutar pruebas específicas
./mvnw test -Dtest=UserServiceImplTest
./mvnw test -Dtest=InmuebleServiceImplTest
./mvnw test -Dtest=UserControllerUnitTest
./mvnw test -Dtest=InmuebleControllerUnitTest

# Ejecutar una prueba específica
./mvnw test -Dtest=UserServiceImplTest#crearUsuario_exitoso
./mvnw test -Dtest=InmuebleServiceImplTest#crearInmueble_exitoso
```

## Tecnologías Utilizadas

- JUnit 5
- Mockito
- Spring Boot Test
- AssertJ (para algunas aserciones)