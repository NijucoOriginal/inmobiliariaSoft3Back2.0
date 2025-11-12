package com.jsebastian.eden.EdenSys.repository;

import com.jsebastian.eden.EdenSys.domain.EstadoTransaccion;
import com.jsebastian.eden.EdenSys.domain.TransaccionInmueble;
import com.jsebastian.eden.EdenSys.domain.Inmueble;
import com.jsebastian.eden.EdenSys.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransaccionInmuebleRepository extends JpaRepository<TransaccionInmueble, Long> {
    Optional<TransaccionInmueble> findTopByInmuebleOrderByFechaActualizacionDesc(Inmueble inmueble);
    List<TransaccionInmueble> findByEstadoActual(EstadoTransaccion estadoActual);
    List<TransaccionInmueble> findByCliente(User cliente);
    Optional<TransaccionInmueble> findByInmueble_Id(Long inmuebleId);
}

