package com.jsebastian.eden.EdenSys.controller;

import com.jsebastian.eden.EdenSys.Dtos.InmuebleDto;
import com.jsebastian.eden.EdenSys.Dtos.InmuebleResponse;
import com.jsebastian.eden.EdenSys.Dtos.UbicacionDto;
import com.jsebastian.eden.EdenSys.domain.*;
import com.jsebastian.eden.EdenSys.services.interfaces.InmuebleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InmuebleControllerUnitTest {

    @Mock
    private InmuebleService inmuebleService;

    @InjectMocks
    private InmuebleController inmuebleController;

    private InmuebleDto inmuebleDto;
    private InmuebleResponse inmuebleResponse;

    @BeforeEach
    void setUp() {
        inmuebleDto = new InmuebleDto(
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

        inmuebleResponse = new InmuebleResponse(
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
                EstadoTransaccion.DISPONIBLE,
                "Bogotá",
                1,
                new ArrayList<>(),
                1,
                "3001234567",
                "Juan Pérez",
                "contacto@example.com",
                new ArrayList<>()
        );
    }

    @Test
    void crearInmueble_exitoso() {
        when(inmuebleService.crearInmueble(any(InmuebleDto.class))).thenReturn(inmuebleResponse);

        ResponseEntity<?> response = inmuebleController.crearInmueble(inmuebleDto);

        System.out.println("[crearInmueble_exitoso] Response: " + response);
        assertEquals(201, response.getStatusCode().value());
        assertInstanceOf(InmuebleResponse.class, response.getBody());
        InmuebleResponse body = (InmuebleResponse) response.getBody();
        assertEquals("Cundinamarca", body.departamento());
        assertEquals("Bogotá", body.ciudad());
    }

    @Test
    void crearInmueble_error_lanzaExcepcion() {
        when(inmuebleService.crearInmueble(any(InmuebleDto.class)))
                .thenThrow(new RuntimeException("Error al crear inmueble"));

        ResponseEntity<?> response = inmuebleController.crearInmueble(inmuebleDto);

        System.out.println("[crearInmueble_error_lanzaExcepcion] Response: " + response);
        assertEquals(500, response.getStatusCode().value()); // El controlador no captura RuntimeException, por lo que se convierte en error 500
        assertNotNull(response.getBody());
    }

    @Test
    void obtenerInmueble_exitoso() {
        Long inmuebleId = 1L;
        when(inmuebleService.obtenerInmueble(inmuebleId)).thenReturn(inmuebleResponse);

        ResponseEntity<?> response = inmuebleController.obtenerInmueble(inmuebleId);

        System.out.println("[obtenerInmueble_exitoso] Response: " + response);
        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(InmuebleResponse.class, response.getBody());
        InmuebleResponse body = (InmuebleResponse) response.getBody();
        assertEquals("Cundinamarca", body.departamento());
        assertEquals("Bogotá", body.ciudad());
    }

    @Test
    void obtenerInmueble_noEncontrado() {
        Long inmuebleId = 999L;
        when(inmuebleService.obtenerInmueble(inmuebleId))
                .thenThrow(new RuntimeException("Inmueble no encontrado"));

        ResponseEntity<?> response = inmuebleController.obtenerInmueble(inmuebleId);

        System.out.println("[obtenerInmueble_noEncontrado] Response: " + response);
        assertEquals(404, response.getStatusCode().value());
        assertInstanceOf(String.class, response.getBody());
        assertTrue(response.getBody().toString().contains("Error:"));
    }

    @Test
    void obtenerListaDeInmuebles_exitoso() {
        List<InmuebleResponse> listaInmuebles = List.of(inmuebleResponse);
        when(inmuebleService.obtenerListaDeInmuebles()).thenReturn(listaInmuebles);

        ResponseEntity<List<InmuebleResponse>> response = inmuebleController.obtenerListaDeInmuebles();

        System.out.println("[obtenerListaDeInmuebles_exitoso] Response: " + response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Cundinamarca", response.getBody().get(0).departamento());
        assertEquals("Bogotá", response.getBody().get(0).ciudad());
    }
}