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

/**
 * Pruebas de integración simplificadas para UserRepository
 */
@DataJpaTest
class UserRepositorySimpleTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    /**
     * Prueba: Guardar y encontrar un usuario por email
     * Verifica que se pueda guardar un usuario y recuperarlo por su email
     */
    @Test
    void testFindByEmail() {
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
    void testExistsByEmail() {
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
}