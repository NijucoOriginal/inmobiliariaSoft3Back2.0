package com.jsebastian.eden.EdenSys.controller;

import com.jsebastian.eden.EdenSys.Dtos.CrearUsuarioDto;
import com.jsebastian.eden.EdenSys.Dtos.UsuarioResponse;
import com.jsebastian.eden.EdenSys.domain.Rol;
import com.jsebastian.eden.EdenSys.domain.User;
import com.jsebastian.eden.EdenSys.exceptions.ValueConflictException;
import com.jsebastian.eden.EdenSys.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private CrearUsuarioDto crearUsuarioDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        crearUsuarioDto = new CrearUsuarioDto("Juan", "Pérez", "12345678", "3001234567", "test@example.com", "Password123@", Rol.USER);
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setRol(Rol.PENDIENTE);
    }

    @Test
    void crearUsuario_exitoso() {
        UsuarioResponse usuarioResponse = new UsuarioResponse("1", "test@example.com", Rol.PENDIENTE);
        when(userService.crearUsuario(any(CrearUsuarioDto.class))).thenReturn(usuarioResponse);

        ResponseEntity<?> response = userController.crearUsuario(crearUsuarioDto);
        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(UsuarioResponse.class, response.getBody());
        UsuarioResponse body = (UsuarioResponse) response.getBody();
        assertEquals("test@example.com", body.email());
        assertEquals(Rol.PENDIENTE, body.rol());
    }

    @Test
    void crearUsuario_emailDuplicado() {
        when(userService.crearUsuario(any(CrearUsuarioDto.class))).thenThrow(new ValueConflictException("Ya existe un usuario con este email: test@example.com"));
        ResponseEntity<?> response = userController.crearUsuario(crearUsuarioDto);
        assertEquals(409, response.getStatusCode().value());
        assertInstanceOf(java.util.Map.class, response.getBody());
        assertEquals("Conflicto de datos", ((java.util.Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    void crearUsuario_errorInesperado() {
        when(userService.crearUsuario(any(CrearUsuarioDto.class))).thenThrow(new RuntimeException("Error inesperado"));
        ResponseEntity<?> response = userController.crearUsuario(crearUsuarioDto);
        assertEquals(500, response.getStatusCode().value());
        assertInstanceOf(java.util.Map.class, response.getBody());
        assertEquals("Error interno del servidor", ((java.util.Map<?, ?>) response.getBody()).get("error"));
    }

    @Test
    void obtenerUsuarioPorId_exitoso() {
        when(userService.buscarPorId(1L)).thenReturn(Optional.of(testUser));
        ResponseEntity<User> response = userController.obtenerUsuarioPorId(1L);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().getEmail());
    }

    @Test
    void obtenerUsuarioPorId_noEncontrado() {
        when(userService.buscarPorId(1L)).thenReturn(Optional.empty());
        ResponseEntity<User> response = userController.obtenerUsuarioPorId(1L);
        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }
}
