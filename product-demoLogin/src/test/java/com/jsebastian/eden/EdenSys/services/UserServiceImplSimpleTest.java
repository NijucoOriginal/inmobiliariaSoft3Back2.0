package com.jsebastian.eden.EdenSys.services;

import com.jsebastian.eden.EdenSys.Dtos.CrearUsuarioDto;
import com.jsebastian.eden.EdenSys.Dtos.UsuarioResponse;
import com.jsebastian.eden.EdenSys.domain.Rol;
import com.jsebastian.eden.EdenSys.domain.User;
import com.jsebastian.eden.EdenSys.exceptions.ValueConflictException;
import com.jsebastian.eden.EdenSys.mappers.UserMapper;
import com.jsebastian.eden.EdenSys.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias simplificadas para UserServiceImpl
 * Estas pruebas se enfocan en la lógica de negocio sin requerir todas las dependencias complejas
 */
class UserServiceImplSimpleTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private com.sendgrid.SendGrid sendGrid;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        // Configurar propiedades inyectadas por @Value
        java.lang.reflect.Field fromEmailField = UserServiceImpl.class.getDeclaredField("fromEmail");
        fromEmailField.setAccessible(true);
        fromEmailField.set(userService, "test@example.com");
        java.lang.reflect.Field fromNameField = UserServiceImpl.class.getDeclaredField("fromName");
        fromNameField.setAccessible(true);
        fromNameField.set(userService, "Test Name");
    }

    /**
     * Prueba: Crear un usuario exitosamente
     * Verifica que un usuario se cree correctamente cuando el email no existe
     */
    @Test
    void testCrearUsuarioExitoso() throws Exception {
        // Arrange
        CrearUsuarioDto crearUsuarioDto = new CrearUsuarioDto(
                "Juan", "Pérez", "12345678", "3001234567", 
                "test@example.com", "Password123@", Rol.USER);
        
        User userEntity = new User();
        userEntity.setEmail("test@example.com");
        userEntity.setContrasena("encodedPassword");
        userEntity.setRol(Rol.PENDIENTE);
        
        UsuarioResponse usuarioResponse = new UsuarioResponse("1", "test@example.com", Rol.PENDIENTE);
        
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userMapper.toEntity(any(CrearUsuarioDto.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(userEntity);
        when(userMapper.toUsuarioResponse(any(User.class))).thenReturn(usuarioResponse);
        // Evitar llamada real a SendGrid
        when(sendGrid.api(any(com.sendgrid.Request.class))).thenReturn(org.mockito.Mockito.mock(com.sendgrid.Response.class));

        // Act
        UsuarioResponse result = userService.crearUsuario(crearUsuarioDto);
        System.out.println("[testCrearUsuarioExitoso] Response: " + result);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.email());
        assertEquals(Rol.PENDIENTE, result.rol());
        
        verify(userRepository).existsByEmail("test@example.com");
        verify(userMapper).toEntity(crearUsuarioDto);
        verify(passwordEncoder).encode("Password123@");
        verify(userRepository).save(userEntity);
        verify(userMapper).toUsuarioResponse(userEntity);
    }

    /**
     * Prueba: Intentar crear un usuario con email duplicado
     * Verifica que se lance una excepción cuando se intenta crear un usuario con email existente
     */
    @Test
    void testCrearUsuarioConEmailDuplicado() {
        // Arrange
        CrearUsuarioDto crearUsuarioDto = new CrearUsuarioDto(
                "Juan", "Pérez", "12345678", "3001234567", 
                "test@example.com", "Password123@", Rol.USER);
        
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        ValueConflictException exception = assertThrows(
                ValueConflictException.class, 
                () -> userService.crearUsuario(crearUsuarioDto));
        System.out.println("[testCrearUsuarioConEmailDuplicado] Exception: " + exception.getMessage());

        assertTrue(exception.getMessage().contains("Ya existe un usuario con este email"));
        verify(userRepository).existsByEmail("test@example.com");
        verify(userMapper, never()).toEntity(any());
    }

    /**
     * Prueba: Buscar usuario por email existente
     * Verifica que se pueda encontrar un usuario por su email
     */
    @Test
    void testBuscarPorEmail() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setRol(Rol.USER);
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.buscarPorEmail(email);
        System.out.println("[testBuscarPorEmail] Response: " + result);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(userRepository).findByEmail(email);
    }

    /**
     * Prueba: Activar usuario con código válido
     * Verifica que un usuario se active correctamente con un código válido
     */
    @Test
    void testActivarUsuarioConCodigoValido() {
        // Arrange
        String codigo = "ABC123";
        User user = new User();
        user.setEmail("test@example.com");
        user.setRol(Rol.PENDIENTE);
        user.setCodigoActivacion(codigo);
        
        when(userRepository.findByCodigoActivacion(codigo)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.activarUsuario(codigo);
        System.out.println("[testActivarUsuarioConCodigoValido] Response: " + result);

        // Assert
        assertTrue(result);
        assertEquals(Rol.CLIENTE, user.getRol());
        assertNull(user.getCodigoActivacion());
        verify(userRepository).findByCodigoActivacion(codigo);
        verify(userRepository).save(user);
    }
}