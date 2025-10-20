package com.jsebastian.eden.EdenSys.repository;

import com.jsebastian.eden.EdenSys.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class InmuebleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InmuebleRepository inmuebleRepository;

    /**
     * Prueba: Guardar y encontrar un inmueble por código
     * Verifica que se pueda guardar un inmueble y recuperarlo por su código
     */
    @Test
    void findByCodigoInmueble_DebeRetornarInmueble_CuandoExiste() {
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
    void existsByCodigoInmueble_DebeRetornarTrue_CuandoInmuebleExiste() {
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
        inmueble.setEstadoTransa(EstadoTransaccion.PERMUTADO);
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

    /**
     * Prueba: Verificar existencia de inmueble por código inexistente
     * Verifica que el método existsByCodigoInmueble retorne false cuando el código no existe
     */
    @Test
    void existsByCodigoInmueble_DebeRetornarFalse_CuandoInmuebleNoExiste() {
        // Act
        boolean exists = inmuebleRepository.existsByCodigoInmueble(999);

        // Assert
        assertThat(exists).isFalse();
    }

    /**
     * Prueba: Buscar inmuebles por ciudad
     * Verifica que se puedan encontrar inmuebles por su ciudad
     */
    @Test
    void findByCiudad_DebeRetornarInmueblesConCiudadEspecificado() {
        // Arrange
        Inmueble inmueble1 = new Inmueble();
        inmueble1.setCodigoInmueble(101);
        inmueble1.setDepartamento("Cundinamarca");
        inmueble1.setCiudad("Bogotá");
        inmueble1.setTipo(TipoInmueble.APARTAMENTO);
        inmueble1.setTipoNegocio(TipoNegocio.VENTA);
        inmueble1.setMedidas(85.5);
        inmueble1.setHabitaciones(3);
        inmueble1.setBanos(2);
        inmueble1.setDescripcion("Hermoso apartamento en el centro");
        inmueble1.setPrecio(350000000);
        inmueble1.setEstado(EstadoInmueble.NUEVO);
        inmueble1.setEstadoTransa(EstadoTransaccion.PERMUTADO);
        inmueble1.setCantidadParqueaderos(1);
        inmueble1.setTelfonoContacto("3001234567");
        inmueble1.setNombreContacto("Juan Pérez");
        inmueble1.setCorreoContacto("contacto@example.com");

        Inmueble inmueble2 = new Inmueble();
        inmueble2.setCodigoInmueble(102);
        inmueble2.setDepartamento("Antioquia");
        inmueble2.setCiudad("Medellín");
        inmueble2.setTipo(TipoInmueble.CASA);
        inmueble2.setTipoNegocio(TipoNegocio.ALQUILER);
        inmueble2.setMedidas(150.0);
        inmueble2.setHabitaciones(4);
        inmueble2.setBanos(3);
        inmueble2.setDescripcion("Casa amplia en Laureles");
        inmueble2.setPrecio(2200000);
        inmueble2.setEstado(EstadoInmueble.USADO);
        inmueble2.setEstadoTransa(EstadoTransaccion.PROCESOCOMPRA);
        inmueble2.setCantidadParqueaderos(2);
        inmueble2.setTelfonoContacto("3109876543");
        inmueble2.setNombreContacto("María González");
        inmueble2.setCorreoContacto("maria@example.com");

        entityManager.persistAndFlush(inmueble1);
        entityManager.persistAndFlush(inmueble2);
        entityManager.clear();

        // Act
        List<Inmueble> inmuebles = inmuebleRepository.findByCiudad("Bogotá");

        // Assert
        assertThat(inmuebles).hasSize(1);
        assertThat(inmuebles.get(0).getCodigoInmueble()).isEqualTo(101);
        assertThat(inmuebles.get(0).getCiudad()).isEqualTo("Bogotá");
    }

    /**
     * Prueba: Buscar inmuebles por tipo de negocio
     * Verifica que se puedan encontrar inmuebles por su tipo de negocio
     */
    @Test
    void findByTipoNegocio_DebeRetornarInmueblesConTipoNegocioEspecificado() {
        // Arrange
        Inmueble inmueble1 = new Inmueble();
        inmueble1.setCodigoInmueble(101);
        inmueble1.setDepartamento("Cundinamarca");
        inmueble1.setCiudad("Bogotá");
        inmueble1.setTipo(TipoInmueble.APARTAMENTO);
        inmueble1.setTipoNegocio(TipoNegocio.VENTA);
        inmueble1.setMedidas(85.5);
        inmueble1.setHabitaciones(3);
        inmueble1.setBanos(2);
        inmueble1.setDescripcion("Hermoso apartamento en el centro");
        inmueble1.setPrecio(350000000);
        inmueble1.setEstado(EstadoInmueble.NUEVO);
        inmueble1.setEstadoTransa(EstadoTransaccion.PROCESOCOMPRA);
        inmueble1.setCantidadParqueaderos(1);
        inmueble1.setTelfonoContacto("3001234567");
        inmueble1.setNombreContacto("Juan Pérez");
        inmueble1.setCorreoContacto("contacto@example.com");

        Inmueble inmueble2 = new Inmueble();
        inmueble2.setCodigoInmueble(102);
        inmueble2.setDepartamento("Antioquia");
        inmueble2.setCiudad("Medellín");
        inmueble2.setTipo(TipoInmueble.CASA);
        inmueble2.setTipoNegocio(TipoNegocio.ALQUILER);
        inmueble2.setMedidas(150.0);
        inmueble2.setHabitaciones(4);
        inmueble2.setBanos(3);
        inmueble2.setDescripcion("Casa amplia en Laureles");
        inmueble2.setPrecio(2200000);
        inmueble2.setEstado(EstadoInmueble.USADO);
        inmueble2.setEstadoTransa(EstadoTransaccion.PROCESOALQUIER);
        inmueble2.setCantidadParqueaderos(2);
        inmueble2.setTelfonoContacto("3109876543");
        inmueble2.setNombreContacto("María González");
        inmueble2.setCorreoContacto("maria@example.com");

        entityManager.persistAndFlush(inmueble1);
        entityManager.persistAndFlush(inmueble2);
        entityManager.clear();

        // Act
        List<Inmueble> inmuebles = inmuebleRepository.findByTipoNegocio(TipoNegocio.VENTA);

        // Assert
        assertThat(inmuebles).hasSize(1);
        assertThat(inmuebles.get(0).getCodigoInmueble()).isEqualTo(101);
        assertThat(inmuebles.get(0).getTipoNegocio()).isEqualTo(TipoNegocio.VENTA);
    }
}