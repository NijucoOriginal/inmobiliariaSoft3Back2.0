package com.jsebastian.eden.EdenSys.services;

import com.jsebastian.eden.EdenSys.Dtos.InmuebleDto;
import com.jsebastian.eden.EdenSys.Dtos.InmueblePatchDto;
import com.jsebastian.eden.EdenSys.Dtos.InmuebleResponse;
import com.jsebastian.eden.EdenSys.domain.*;
import com.jsebastian.eden.EdenSys.exceptions.InmuebleException;
import com.jsebastian.eden.EdenSys.exceptions.ResourceNotFoundException;
import com.jsebastian.eden.EdenSys.mappers.InmuebleMapper;
import com.jsebastian.eden.EdenSys.repository.InmuebleRepository;
import com.jsebastian.eden.EdenSys.services.interfaces.InmuebleService;
import com.jsebastian.eden.EdenSys.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InmuebleServiceImpl implements InmuebleService {

    @Autowired
    private InmuebleRepository inmuebleRepository;
    @Autowired
    private InmuebleMapper inmuebleMapper;
    @Autowired
    private UserService userService;


    @Override
    public InmuebleResponse crearInmueble(InmuebleDto inmuebleDto) {
        try {

            try { //-------debemos verificar que sea un user con token habil extreyendo su email y que se valido crear el inmueble
                String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
                var optionalUser = userService.buscarPorEmail(userEmail);
                if (optionalUser.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario creador no existe");
                }
                User userAux = optionalUser.get();
                if (userAux.getRol() != Rol.CLIENTE) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario aun no es cliente del sistema");
                }
                
                // Convertir el DTO a entidad
                var nuevoInmueble = inmuebleMapper.toEntity(inmuebleDto);
                
                // Establecer el propietario
                Cliente propietario = new Cliente();
                propietario.setId(userAux.getId());
                nuevoInmueble.setPropietario(propietario);
                
                // Establecer la referencia al inmueble en las imágenes
                if (nuevoInmueble.getImagenes() != null) {
                    for (Imagen imagen : nuevoInmueble.getImagenes()) {
                        imagen.setInmueble(nuevoInmueble);
                    }
                }
                
                // Establecer estado inicial
                nuevoInmueble.setEstadoPosteoInmueble(EstadoPosteoInmueble.PENDIENTE);
                
                // Guardar el inmueble
                inmuebleRepository.save(nuevoInmueble);
                
                return inmuebleMapper.toResponse(nuevoInmueble);
            } catch (Exception e) {
                throw new InmuebleException(e);
            }
        } catch (InmuebleException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminarInmueble(Long id) {
        if (!inmuebleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inmueble no encontrado con id: " + id);
        }
        inmuebleRepository.deleteById(id);
    }


    @Override
    public InmuebleResponse actualizarInmueble(Long id, InmuebleDto inmuebleDto) {
        try {
            var inmuebleExistente = inmuebleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con id: " + id));
            inmuebleMapper.updateEntityFromDto(inmuebleDto, inmuebleExistente);
            inmuebleRepository.save(inmuebleExistente);
            return inmuebleMapper.toResponse(inmuebleExistente);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el inmueble: " + e.getMessage(), e);
        }
    }

    @Override
    public InmuebleResponse patchInmueble(Long id, InmueblePatchDto patchDto) {
        try {
            var inmueble = inmuebleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con id: " + id));
            if (patchDto.departamento() != null) inmueble.setDepartamento(patchDto.departamento());
            // Ubicacion: no es un enum, es un objeto. No se puede actualizar por PATCH con un String.
            if (patchDto.tipoNegocio() != null) {
                try {
                    inmueble.setTipoNegocio(TipoNegocio.valueOf(patchDto.tipoNegocio()));
                } catch (Exception e) {
                    throw new RuntimeException("Tipo de negocio inválido: " + patchDto.tipoNegocio());
                }
            }
            if (patchDto.tipo() != null) {
                try {
                    inmueble.setTipo(TipoInmueble.valueOf(patchDto.tipo()));
                } catch (Exception e) {
                    throw new RuntimeException("Tipo de inmueble inválido: " + patchDto.tipo());
                }
            }
            if (patchDto.medidas() != null) inmueble.setMedidas(Double.parseDouble(patchDto.medidas()));
            if (patchDto.habitaciones() != null) inmueble.setHabitaciones(patchDto.habitaciones());
            if (patchDto.banos() != null) inmueble.setBanos(patchDto.banos());
            if (patchDto.descripcion() != null) inmueble.setDescripcion(patchDto.descripcion());
            if (patchDto.estado() != null) {
                try {
                    inmueble.setEstado(EstadoInmueble.valueOf(patchDto.estado()));
                } catch (Exception e) {
                    throw new RuntimeException("Estado de inmueble inválido: " + patchDto.estado());
                }
            }
            if (patchDto.precio() != null) inmueble.setPrecio(patchDto.precio());
            if (patchDto.estadoTransa() != null) {
                try {
                    inmueble.setEstadoTransa(EstadoTransaccion.valueOf(patchDto.estadoTransa()));
                } catch (Exception e) {
                    throw new RuntimeException("Estado de transacción inválido: " + patchDto.estadoTransa());
                }
            }
            if (patchDto.ciudad() != null) inmueble.setCiudad(patchDto.ciudad());
            if (patchDto.codigoInmueble() != null) inmueble.setCodigoInmueble(Integer.parseInt(patchDto.codigoInmueble()));
            if (patchDto.cantidadParqueaderos() != null) inmueble.setCantidadParqueaderos(patchDto.cantidadParqueaderos());
            if (patchDto.telfonoContacto() != null) inmueble.setTelfonoContacto(patchDto.telfonoContacto());
            if (patchDto.nombreContacto() != null) inmueble.setNombreContacto(patchDto.nombreContacto());
            if (patchDto.correoContacto() != null) inmueble.setCorreoContacto(patchDto.correoContacto());
            if (patchDto.imagenes() != null) {
                throw new UnsupportedOperationException("Actualización de imágenes no implementada");
                // inmueble.setImagenes(listaDeImagenes);
            }
            // No se actualizan: agenteAsociado, asesorLegal, historial, documentosImportantes, ubicacion
            inmuebleRepository.save(inmueble);
            return inmuebleMapper.toResponse(inmueble);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar parcialmente el inmueble: " + e.getMessage(), e);
        }
    }

    @Override
    public InmuebleResponse obtenerInmueble(Long id) {
        try {
            var inmueble = inmuebleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con id: " + id));
            return inmuebleMapper.toResponse(inmueble);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el inmueble: " + e.getMessage(), e);
        }
    }

    @Override
    public List<InmuebleResponse> obtenerListaDeInmuebles() {
        try {
            var lista = inmuebleRepository.findAll();
            return lista.stream().map(inmuebleMapper::toResponse).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la lista de inmuebles: " + e.getMessage(), e);
        }
    }

}