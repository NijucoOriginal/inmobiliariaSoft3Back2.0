package com.jsebastian.eden.EdenSys.services;

import com.jsebastian.eden.EdenSys.Dtos.InmuebleDto;
import com.jsebastian.eden.EdenSys.Dtos.InmuebleResponse;
import com.jsebastian.eden.EdenSys.Dtos.UbicacionDto;
import com.jsebastian.eden.EdenSys.domain.*;
import com.jsebastian.eden.EdenSys.exceptions.InmuebleException;
import com.jsebastian.eden.EdenSys.mappers.InmuebleMapper;
import com.jsebastian.eden.EdenSys.repository.ClienteRepository;
import com.jsebastian.eden.EdenSys.repository.InmuebleRepository;
import com.jsebastian.eden.EdenSys.repository.UbicacionRepository;
import com.jsebastian.eden.EdenSys.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InmuebleServiceImplTest {

    @Mock
    private InmuebleRepository inmuebleRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private UbicacionRepository ubicacionRepository;

    @Mock
    private InmuebleMapper inmuebleMapper;

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private InmuebleServiceImpl inmuebleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup security context
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
    }

    @Test
    void crearInmueble_exitoso() {
        // Arrange
        InmuebleDto inmuebleDto = new InmuebleDto(
                "Cundinamarca",
                "Bogotá",
                new UbicacionDto(4.0, -74.0),
                "VENTA",
                "APARTAMENTO",
                85.5,
                3,
                2,
                "Hermoso apartamento en el centro",
                350000000,
                1,
                "3001234567",
                "Juan Pérez",
                "contacto@example.com",
                new ArrayList<>()
        );

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setRol(Rol.CLIENTE);
        user.setDocumentoIdentidad("12345678");

        Inmueble inmueble = new Inmueble();
        inmueble.setId(1L);
        inmueble.setDepartamento("Cundinamarca");
        inmueble.setCiudad("Bogotá");
        inmueble.setTipo(TipoInmueble.APARTAMENTO);
        inmueble.setTipoNegocio(TipoNegocio.VENTA);
        inmueble.setMedidas(85.5);
        inmueble.setHabitaciones(3);
        inmueble.setBanos(2);
        inmueble.setDescripcion("Hermoso apartamento en el centro");
        inmueble.setPrecio(350000000);
        inmueble.setCantidadParqueaderos(1);
        inmueble.setTelfonoContacto("3001234567");
        inmueble.setNombreContacto("Juan Pérez");
        inmueble.setCorreoContacto("contacto@example.com");
        inmueble.setEstadoPosteoInmueble(EstadoPosteoInmueble.PENDIENTE);

        InmuebleResponse inmuebleResponse = new InmuebleResponse(
                "Cundinamarca",
                new Ubicacion(),
                TipoNegocio.VENTA,
                new AgenteInmobiliario(),
                new ArrayList<>(),
                TipoInmueble.APARTAMENTO,
                85.5,
                3,
                2,
                "Hermoso apartamento en el centro",
                EstadoInmueble.NUEVO,
                350000000,
                EstadoTransaccion.PROCESOALQUIER,
                "Bogotá",
                1,
                new ArrayList<>(),
                1,
                "3001234567",
                "Juan Pérez",
                "contacto@example.com",
                new ArrayList<>()
        );

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setDocumentoIdentidad("12345678");

        when(userService.buscarPorEmail("test@example.com")).thenReturn(Optional.of(user));
        when(inmuebleMapper.toEntity(inmuebleDto)).thenReturn(inmueble);
        when(clienteRepository.findByDocumentoIdentidad("12345678")).thenReturn(Optional.of(cliente));
        when(inmuebleRepository.save(any(Inmueble.class))).thenReturn(inmueble);
        when(inmuebleMapper.toResponse(inmueble)).thenReturn(inmuebleResponse);

        // Act
        InmuebleResponse result = inmuebleService.crearInmueble(inmuebleDto);

        // Assert
        System.out.println("[crearInmueble_exitoso] Response: " + result);
        assertNotNull(result);
        assertEquals("Cundinamarca", result.departamento());
        assertEquals("Bogotá", result.ciudad());
        verify(userService).buscarPorEmail("test@example.com");
        verify(inmuebleMapper).toEntity(inmuebleDto);
        verify(inmuebleRepository).save(any(Inmueble.class));
        verify(inmuebleMapper).toResponse(inmueble);
    }

    @Test
    void crearInmueble_usuarioNoEncontrado_lanzaExcepcion() {
        // Arrange
        InmuebleDto inmuebleDto = new InmuebleDto(
                "Cundinamarca",
                "Bogotá",
                new UbicacionDto(4.0, -74.0),
                "VENTA",
                "APARTAMENTO",
                85.5,
                3,
                2,
                "Hermoso apartamento en el centro",
                350000000,
                1,
                "3001234567",
                "Juan Pérez",
                "contacto@example.com",
                new ArrayList<>()
        );

        when(userService.buscarPorEmail("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inmuebleService.crearInmueble(inmuebleDto);
        });

        System.out.println("[crearInmueble_usuarioNoEncontrado_lanzaExcepcion] Exception: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Error al crear el inmueble"));
        verify(userService).buscarPorEmail("test@example.com");
        verify(inmuebleRepository, never()).save(any());
    }

    @Test
    void crearInmueble_usuarioNoEsCliente_lanzaExcepcion() {
        // Arrange
        InmuebleDto inmuebleDto = new InmuebleDto(
                "Cundinamarca",
                "Bogotá",
                new UbicacionDto(4.0, -74.0),
                "VENTA",
                "APARTAMENTO",
                85.5,
                3,
                2,
                "Hermoso apartamento en el centro",
                350000000,
                1,
                "3001234567",
                "Juan Pérez",
                "contacto@example.com",
                new ArrayList<>()
        );

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setRol(Rol.PENDIENTE); // Not a CLIENTE
        user.setDocumentoIdentidad("12345678");

        when(userService.buscarPorEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inmuebleService.crearInmueble(inmuebleDto);
        });

        System.out.println("[crearInmueble_usuarioNoEsCliente_lanzaExcepcion] Exception: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Error al crear el inmueble"));
        verify(userService).buscarPorEmail("test@example.com");
        verify(inmuebleRepository, never()).save(any());
    }

    @Test
    void obtenerInmueble_exitoso() {
        // Arrange
        Long inmuebleId = 1L;
        Inmueble inmueble = new Inmueble();
        inmueble.setId(inmuebleId);
        inmueble.setDepartamento("Cundinamarca");
        inmueble.setCiudad("Bogotá");

        InmuebleResponse inmuebleResponse = new InmuebleResponse(
                "Cundinamarca",
                new Ubicacion(),
                TipoNegocio.VENTA,
                new AgenteInmobiliario(),
                new ArrayList<>(),
                TipoInmueble.APARTAMENTO,
                85.5,
                3,
                2,
                "Hermoso apartamento en el centro",
                EstadoInmueble.NUEVO,
                350000000,
                EstadoTransaccion.PROCESOALQUIER,
                "Bogotá",
                1,
                new ArrayList<>(),
                1,
                "3001234567",
                "Juan Pérez",
                "contacto@example.com",
                new ArrayList<>()
        );

        when(inmuebleRepository.findById(inmuebleId)).thenReturn(Optional.of(inmueble));
        when(inmuebleMapper.toResponse(inmueble)).thenReturn(inmuebleResponse);

        // Act
        InmuebleResponse result = inmuebleService.obtenerInmueble(inmuebleId);

        // Assert
        System.out.println("[obtenerInmueble_exitoso] Response: " + result);
        assertNotNull(result);
        assertEquals("Cundinamarca", result.departamento());
        assertEquals("Bogotá", result.ciudad());
        verify(inmuebleRepository).findById(inmuebleId);
        verify(inmuebleMapper).toResponse(inmueble);
    }

    @Test
    void obtenerInmueble_noEncontrado_lanzaExcepcion() {
        // Arrange
        Long inmuebleId = 999L;
        when(inmuebleRepository.findById(inmuebleId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inmuebleService.obtenerInmueble(inmuebleId);
        });

        System.out.println("[obtenerInmueble_noEncontrado_lanzaExcepcion] Exception: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Error al obtener el inmueble"));
        verify(inmuebleRepository).findById(inmuebleId);
    }
}