package com.jsebastian.eden.EdenSys.services;

import com.jsebastian.eden.EdenSys.Dtos.*;
import com.jsebastian.eden.EdenSys.domain.EstadoTransaccion;
import com.jsebastian.eden.EdenSys.domain.Inmueble;
import com.jsebastian.eden.EdenSys.domain.Rol;
import com.jsebastian.eden.EdenSys.domain.TransaccionInmueble;
import com.jsebastian.eden.EdenSys.domain.User;
import com.jsebastian.eden.EdenSys.exceptions.EstadoInvalidoException;
import com.jsebastian.eden.EdenSys.exceptions.InmuebleNotFoundException;
import com.jsebastian.eden.EdenSys.exceptions.UnauthorizedAccessException;
import com.jsebastian.eden.EdenSys.mappers.InmuebleMapper;
import com.jsebastian.eden.EdenSys.repository.InmuebleRepository;
import com.jsebastian.eden.EdenSys.repository.TransaccionInmuebleRepository;
import com.jsebastian.eden.EdenSys.repository.UserRepository;
import com.jsebastian.eden.EdenSys.services.interfaces.NegociacionInmuebleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NegociacionInmuebleServiceImpl implements NegociacionInmuebleService {

    private final InmuebleRepository inmuebleRepository;
    private final TransaccionInmuebleRepository transaccionInmuebleRepository;
    private final UserRepository userRepository;
    private final InmuebleMapper inmuebleMapper;

    private static final Map<EstadoTransaccion, Set<EstadoTransaccion>> TRANSICIONES_PERMITIDAS = Map.of(
            EstadoTransaccion.PENDIENTE, Set.of(EstadoTransaccion.PROCESOCOMPRA, EstadoTransaccion.PROCESOALQUIER, EstadoTransaccion.PROCESOPERMUTACION),
            EstadoTransaccion.PROCESOCOMPRA, Set.of(EstadoTransaccion.VENDIDO, EstadoTransaccion.NOADMITIDA),
            EstadoTransaccion.PROCESOALQUIER, Set.of(EstadoTransaccion.ALQUILADO, EstadoTransaccion.NOADMITIDA),
            EstadoTransaccion.PROCESOPERMUTACION, Set.of(EstadoTransaccion.PERMUTADO, EstadoTransaccion.NOADMITIDA)
    );

    @Override
    public List<InmuebleAsignadoResponse> obtenerInmueblesAsignados(Long agenteId) {
        User agente = userRepository.findById(agenteId).orElseThrow(() -> new UnauthorizedAccessException("Agente no encontrado"));
        if(agente.getRol() != Rol.AGENTE){
            throw new UnauthorizedAccessException("El usuario no tiene rol AGENTE");
        }
        List<Inmueble> inmuebles = inmuebleRepository.findByAgenteAsociado(agente);
        return inmuebles.stream()
                .map(inmueble -> new InmuebleAsignadoResponse(
                        inmueble.getId(),
                        inmueble.getDescripcion(),
                        inmueble.getTipo()!=null?inmueble.getTipo().name():null,
                        inmueble.getEstadoTransa()!=null?inmueble.getEstadoTransa().name():null,
                        inmueble.getPrecio()))
                .collect(Collectors.toList());
    }

    @Override
    public CambioEstadoResponse cambiarEstadoInmueble(Long inmuebleId, CambiarEstadoInmuebleRequest dto, Long agenteId) {
        Inmueble inmueble = inmuebleRepository.findById(inmuebleId).orElseThrow(() -> new InmuebleNotFoundException("Inmueble no encontrado"));
        User agente = userRepository.findById(agenteId).orElseThrow(() -> new UnauthorizedAccessException("Agente no encontrado"));
        if(agente.getRol() != Rol.AGENTE){
            throw new UnauthorizedAccessException("El usuario no tiene rol AGENTE");
        }
        if(inmueble.getAgenteAsociado()==null || !Objects.equals(inmueble.getAgenteAsociado().getId(), agenteId)){
            throw new UnauthorizedAccessException("El inmueble no pertenece al agente");
        }
        EstadoTransaccion estadoActual = inmueble.getEstadoTransa();
        EstadoTransaccion nuevoEstado;
        try {
            nuevoEstado = EstadoTransaccion.valueOf(dto.nuevoEstado());
        } catch (IllegalArgumentException e){
            throw new EstadoInvalidoException("Estado de transacción no válido");
        }
        if(estadoActual == nuevoEstado){
            throw new EstadoInvalidoException("El inmueble ya está en el estado especificado");
        }
        if(!TRANSICIONES_PERMITIDAS.getOrDefault(estadoActual, Set.of()).contains(nuevoEstado)){
            throw new EstadoInvalidoException("Transición no permitida de " + estadoActual + " a " + nuevoEstado);
        }
        // Persistir historial de transacción
        TransaccionInmueble transaccion = TransaccionInmueble.builder()
                .inmueble(inmueble)
                .estadoAnterior(estadoActual)
                .estadoActual(nuevoEstado)
                .fechaActualizacion(LocalDateTime.now())
                .comentarioAgente(dto.comentario())
                .build();
        transaccionInmuebleRepository.save(transaccion);
        // Actualizar estado del inmueble
        inmueble.setEstadoTransa(nuevoEstado);
        inmuebleRepository.save(inmueble);
        return inmuebleMapper.toCambioEstadoResponse(inmueble, estadoActual!=null?estadoActual.name():null, nuevoEstado.name(), transaccion.getFechaActualizacion());
    }

    @Override
    public ClienteAsociadoResponse obtenerClienteAsociado(Long inmuebleId) {
        Inmueble inmueble = inmuebleRepository.findById(inmuebleId).orElseThrow(() -> new InmuebleNotFoundException("Inmueble no encontrado"));
        User cliente = inmueble.getPropietario(); // Asumiendo propietario como cliente
        return inmuebleMapper.toClienteAsociadoResponse(cliente);
    }
}

