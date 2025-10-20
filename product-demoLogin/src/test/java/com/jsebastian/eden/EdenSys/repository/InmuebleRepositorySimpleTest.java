package com.jsebastian.eden.EdenSys.repository;

import com.jsebastian.eden.EdenSys.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración simplificadas para InmuebleRepository
 */
@DataJpaTest
class InmuebleRepositorySimpleTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InmuebleRepository inmuebleRepository;

    /**
     * Prueba: Guardar y encontrar un inmueble por código
     * Verifica que se pueda guardar un inmueble y recuperarlo por su código
     */
    @Test
    void testFindByCodigoInmueble() {
        // Arrange
        Inmueble inmueble = new Inmueble();
        inmueble.setCodigoInmueble(101);
        inmueble.setDepartamento("Cundinamarca");
        inmueble.setCiudad("Bogotá");
        inmueble.setTipo(TipoInmueble.APARTAMENTO);
        inmueble.setTipoNegocio(TipoNegocio.VENTA);
        inmueble.setMedidas(85.5);
        inmueble.setHabitaciones(3);
        inmueble.setBanos(2);
        inmueble.setDescripcion("Hermoso apartamento en el centro");
        inmueble.setPrecio(350000000);
        inmueble.setEstado(EstadoInmueble.NUEVO);
        inmueble.setEstadoTransa(EstadoTransaccion.PROCESOALQUIER);
        inmueble.setCantidadParqueaderos(1);
        inmueble.setTelfonoContacto("3001234567");
        inmueble.setNombreContacto("Juan Pérez");
        inmueble.setCorreoContacto("contacto@example.com");

        entityManager.persistAndFlush(inmueble);
        entityManager.detach(inmueble);

        // Act
        Optional<Inmueble> found = inmuebleRepository.findByCodigoInmueble(101);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getCodigoInmueble()).isEqualTo(101);
        assertThat(found.get().getDepartamento()).isEqualTo("Cundinamarca");
        assertThat(found.get().getCiudad()).isEqualTo("Bogotá");
    }

    /**
     * Prueba: Verificar existencia de inmueble por código
     * Verifica que el método existsByCodigoInmueble funcione correctamente
     */
    @Test
    void testExistsByCodigoInmueble() {
        // Arrange
        Inmueble inmueble = new Inmueble();
        inmueble.setCodigoInmueble(101);
        inmueble.setDepartamento("Cundinamarca");
        inmueble.setCiudad("Bogotá");
        inmueble.setTipo(TipoInmueble.APARTAMENTO);
        inmueble.setTipoNegocio(TipoNegocio.VENTA);
        inmueble.setMedidas(85.5);
        inmueble.setHabitaciones(3);
        inmueble.setBanos(2);
        inmueble.setDescripcion("Hermoso apartamento en el centro");
        inmueble.setPrecio(350000000);
        inmueble.setEstado(EstadoInmueble.NUEVO);
        inmueble.setEstadoTransa(EstadoTransaccion.PROCESOCOMPRA);
        inmueble.setCantidadParqueaderos(1);
        inmueble.setTelfonoContacto("3001234567");
        inmueble.setNombreContacto("Juan Pérez");
        inmueble.setCorreoContacto("contacto@example.com");

        entityManager.persistAndFlush(inmueble);
        entityManager.detach(inmueble);

        // Act
        boolean exists = inmuebleRepository.existsByCodigoInmueble(101);

        // Assert
        assertThat(exists).isTrue();
    }
}