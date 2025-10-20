package com.jsebastian.eden.EdenSys.services;

import com.jsebastian.eden.EdenSys.Dtos.CrearUsuarioDto;
import com.jsebastian.eden.EdenSys.Dtos.UsuarioResponse;
import com.jsebastian.eden.EdenSys.domain.Rol;
import com.jsebastian.eden.EdenSys.domain.User;
import com.jsebastian.eden.EdenSys.exceptions.ValueConflictException;
import com.jsebastian.eden.EdenSys.mappers.UserMapper;
import com.jsebastian.eden.EdenSys.repository.UserRepository;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private SendGrid sendGrid;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearUsuario_exitoso() {
        CrearUsuarioDto dto = new CrearUsuarioDto("Juan", "Pérez", "12345678", "3001234567", "test@example.com", "Password123@", Rol.USER);
        User userEntity = new User();
        userEntity.setEmail("test@example.com");
        userEntity.setNombre("Juan");
        userEntity.setRol(Rol.PENDIENTE);
        UsuarioResponse usuarioResponse = new UsuarioResponse("1", "test@example.com", Rol.PENDIENTE);

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(userEntity);
        when(passwordEncoder.encode(dto.contrasena())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(userEntity);
        when(userMapper.toUsuarioResponse(any(User.class))).thenReturn(usuarioResponse);

        UsuarioResponse result = userService.crearUsuario(dto);
        System.out.println("[crearUsuario_exitoso] Response: " + result);
        assertEquals("test@example.com", result.email());
        assertEquals(Rol.PENDIENTE, result.rol());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void crearUsuario_emailDuplicado_lanzaExcepcion() {
        CrearUsuarioDto dto = new CrearUsuarioDto("Juan", "Pérez", "12345678", "3001234567", "test@example.com", "Password123@", Rol.USER);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        Exception ex = assertThrows(ValueConflictException.class, () -> userService.crearUsuario(dto));
        System.out.println("[crearUsuario_emailDuplicado_lanzaExcepcion] Exception: " + ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void crearUsuario_errorEnMapper_lanzaRuntimeException() {
        CrearUsuarioDto dto = new CrearUsuarioDto("Juan", "Pérez", "12345678", "3001234567", "test2@example.com", "Password123@", Rol.USER);
        when(userRepository.existsByEmail("test2@example.com")).thenReturn(false);
        when(userMapper.toEntity(dto)).thenThrow(new RuntimeException("Error en el mapper"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.crearUsuario(dto));
        System.out.println("[crearUsuario_errorEnMapper_lanzaRuntimeException] Exception: " + ex.getMessage());
        assertTrue(ex.getMessage().contains("Error al crear el usuario"));
    }
}
