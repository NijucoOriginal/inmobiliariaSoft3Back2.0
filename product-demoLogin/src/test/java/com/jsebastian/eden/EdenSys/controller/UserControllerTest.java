package com.jsebastian.eden.EdenSys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsebastian.eden.EdenSys.Dtos.CrearUsuarioDto;
import com.jsebastian.eden.EdenSys.Dtos.UsuarioResponse;
import com.jsebastian.eden.EdenSys.domain.Rol;
import com.jsebastian.eden.EdenSys.domain.User;
import com.jsebastian.eden.EdenSys.exceptions.ValueConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private com.jsebastian.eden.EdenSys.services.interfaces.UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private CrearUsuarioDto crearUsuarioDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setRol(Rol.USER);

        crearUsuarioDto = new CrearUsuarioDto(
                "Juan", "Pérez", "12345678", "3001234567",
                "test@example.com", "Password123@", Rol.USER);
    }

    /**
     * Prueba: Crear un usuario exitosamente
     * Verifica que se pueda crear un usuario correctamente a través del endpoint POST /api/usuarios
     */
    @Test
    void crearUsuario_DebeRetornarUsuario_CuandoEsExitoso() throws Exception {
        // Arrange
        UsuarioResponse usuarioResponse = new UsuarioResponse("1", "test@example.com", Rol.USER);
        when(userService.crearUsuario(any(CrearUsuarioDto.class))).thenReturn(usuarioResponse);

        // Act & Assert
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(crearUsuarioDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).crearUsuario(any(CrearUsuarioDto.class));
    }

    /**
     * Prueba: Crear un usuario con email duplicado
     * Verifica que se retorne un error 409 cuando se intenta crear un usuario con email existente
     */
    @Test
    void crearUsuario_DebeRetornar409_CuandoEmailYaExiste() throws Exception {
        // Arrange
        when(userService.crearUsuario(any(CrearUsuarioDto.class)))
                .thenThrow(new ValueConflictException("Ya existe un usuario con este email: test@example.com"));

        // Act & Assert
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(crearUsuarioDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflicto de datos"));

        verify(userService).crearUsuario(any(CrearUsuarioDto.class));
    }

    /**
     * Prueba: Obtener todos los usuarios
     * Verifica que se puedan obtener todos los usuarios a través del endpoint GET /api/usuarios
     */
    @Test
    void obtenerTodosLosUsuarios_DebeRetornarListaDeUsuarios() throws Exception {
        // Arrange
        when(userService.obtenerTodosLosUsuarios()).thenReturn(java.util.List.of(testUser));

        // Act & Assert
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(userService).obtenerTodosLosUsuarios();
    }

    /**
     * Prueba: Obtener usuario por ID existente
     * Verifica que se pueda obtener un usuario por su ID a través del endpoint GET /api/usuarios/{id}
     */
    @Test
    void obtenerUsuarioPorId_DebeRetornarUsuario_CuandoExiste() throws Exception {
        // Arrange
        when(userService.buscarPorId(1L)).thenReturn(Optional.of(testUser));

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).buscarPorId(1L);
    }

    /**
     * Prueba: Obtener usuario por ID inexistente
     * Verifica que se retorne 404 cuando se busca un usuario con ID que no existe
     */
    @Test
    void obtenerUsuarioPorId_DebeRetornar404_CuandoNoExiste() throws Exception {
        // Arrange
        when(userService.buscarPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/999"))
                .andExpect(status().isNotFound());

        verify(userService).buscarPorId(999L);
    }

    /**
     * Prueba: Eliminar usuario por ID
     * Verifica que se pueda eliminar un usuario por su ID a través del endpoint DELETE /api/usuarios/{id}
     */
    @Test
    void eliminarUsuario_DebeRetornar204_CuandoEsExitoso() throws Exception {
        // Arrange
        doNothing().when(userService).eliminarUsuario(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(userService).eliminarUsuario(1L);
    }
}