package com.jsebastian.eden.EdenSys.controller;

import com.jsebastian.eden.EdenSys.Dtos.*;
import com.jsebastian.eden.EdenSys.domain.User;
import com.jsebastian.eden.EdenSys.repository.UserRepository;
import com.jsebastian.eden.EdenSys.services.interfaces.NegociacionInmuebleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inmuebles/negociacion")
@RequiredArgsConstructor
public class ControllerNegociacionInmuebles {

    private final NegociacionInmuebleService negociacionInmuebleService;
    private final UserRepository userRepository;

    private Long getAgenteIdDesdeContexto(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return user.getId();
    }

    @GetMapping("/asignados")
    public ResponseEntity<List<InmuebleAsignadoResponse>> cargarInmueblesAsignados(){
        Long agenteId = getAgenteIdDesdeContexto();
        return ResponseEntity.ok(negociacionInmuebleService.obtenerInmueblesAsignados(agenteId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<CambioEstadoResponse> cambiarEstadoTransaccion(@PathVariable Long id, @RequestBody CambiarEstadoInmuebleRequest request){
        Long agenteId = getAgenteIdDesdeContexto();
        return ResponseEntity.ok(negociacionInmuebleService.cambiarEstadoInmueble(id, request, agenteId));
    }

    @GetMapping("/{id}/cliente")
    public ResponseEntity<ClienteAsociadoResponse> obtenerCliente(@PathVariable Long id){
        return ResponseEntity.ok(negociacionInmuebleService.obtenerClienteAsociado(id));
    }
}

