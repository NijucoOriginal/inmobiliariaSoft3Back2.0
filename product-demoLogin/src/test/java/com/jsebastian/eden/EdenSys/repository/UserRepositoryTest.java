package com.jsebastian.eden.EdenSys.repository;

import com.jsebastian.eden.EdenSys.domain.Rol;
import com.jsebastian.eden.EdenSys.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    /**
     * Prueba: Guardar y encontrar un usuario por email
     * Verifica que se pueda guardar un usuario y recuperarlo por su email
     */
    @Test
    void findByEmail_DebeRetornarUsuario_CuandoExiste() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setContrasena("password");
        user.setRol(Rol.USER);
        user.setNombre("Juan");
        user.setApellido("Pérez");
        user.setDocumentoIdentidad("12345678");
        user.setTelefono("3001234567");

        entityManager.persistAndFlush(user);
        entityManager.detach(user);

        // Act
        Optional<User> found = userRepository.findByEmail("test@example.com");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getNombre()).isEqualTo("Juan");
    }

    /**
     * Prueba: Verificar existencia de usuario por email
     * Verifica que el método existsByEmail funcione correctamente
     */
    @Test
    void existsByEmail_DebeRetornarTrue_CuandoEmailExiste() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setContrasena("password");
        user.setRol(Rol.USER);
        user.setNombre("Juan");
        user.setApellido("Pérez");
        user.setDocumentoIdentidad("12345678");
        user.setTelefono("3001234567");

        entityManager.persistAndFlush(user);
        entityManager.detach(user);

        // Act
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Assert
        assertThat(exists).isTrue();
    }

    /**
     * Prueba: Verificar existencia de usuario por email inexistente
     * Verifica que el método existsByEmail retorne false cuando el email no existe
     */
    @Test
    void existsByEmail_DebeRetornarFalse_CuandoEmailNoExiste() {
        // Act
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Assert
        assertThat(exists).isFalse();
    }

    /**
     * Prueba: Buscar usuarios por rol
     * Verifica que se puedan encontrar usuarios por su rol
     */
    @Test
    void findByRol_DebeRetornarUsuariosConRolEspecificado() {
        // Arrange
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setContrasena("password");
        user1.setRol(Rol.USER);
        user1.setNombre("Juan");
        user1.setApellido("Pérez");
        user1.setDocumentoIdentidad("12345678");
        user1.setTelefono("3001234567");

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setContrasena("password");
        user2.setRol(Rol.ADMIN);
        user2.setNombre("María");
        user2.setApellido("González");
        user2.setDocumentoIdentidad("87654321");
        user2.setTelefono("3007654321");

        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);
        entityManager.clear();

        // Act
        List<User> users = userRepository.findByRol(Rol.USER);

        // Assert
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getEmail()).isEqualTo("user1@example.com");
        assertThat(users.get(0).getRol()).isEqualTo(Rol.USER);
    }
}