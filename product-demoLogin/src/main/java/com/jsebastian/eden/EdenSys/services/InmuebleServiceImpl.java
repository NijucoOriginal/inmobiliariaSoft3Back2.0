package com.jsebastian.eden.EdenSys.services;

import com.jsebastian.eden.EdenSys.Dtos.InmuebleDto;
import com.jsebastian.eden.EdenSys.Dtos.InmueblePatchDto;
import com.jsebastian.eden.EdenSys.Dtos.InmuebleResponse;
import com.jsebastian.eden.EdenSys.domain.*;
import com.jsebastian.eden.EdenSys.mappers.InmuebleMapper;
import com.jsebastian.eden.EdenSys.repository.InmuebleRepository;
import com.jsebastian.eden.EdenSys.repository.UserRepository;
import com.jsebastian.eden.EdenSys.services.interfaces.InmuebleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InmuebleServiceImpl implements InmuebleService {

    @Autowired
    private  InmuebleRepository inmuebleRepository;
    @Autowired
    private  InmuebleMapper inmuebleMapper;

    @Autowired
    private  UserRepository userRepository;



    @Override
    public InmuebleResponse crearInmueble(InmuebleDto inmuebleDto,
                                          List<MultipartFile> imagenes,
                                          List<MultipartFile> documentosImportantes) {
        try {
            Inmueble nuevoInmueble = inmuebleMapper.toEntity(inmuebleDto);

            User agenteMenorCarga = buscarAgenteConMenorCarga();
            nuevoInmueble.setEstadoTransa(EstadoTransaccion.PENDIENTE);
            nuevoInmueble.setAgenteAsociado(agenteMenorCarga);
            nuevoInmueble.setAsesorLegal(agenteMenorCarga);


            // 4️⃣ Guardar las imágenes si existen
            if (imagenes != null && !imagenes.isEmpty()) {
                for (MultipartFile imagen : imagenes) {
                    String ruta = guardarArchivo(imagen, "uploads/imagenes/");
                    Imagen img = new Imagen();
                    img.setRuta(ruta);
                    img.setInmueble(nuevoInmueble);
                    // Guarda en tu repositorio de imágenes
                    imagenRepository.save(img);
                }
            }

            // 5️⃣ Guardar los documentos importantes si existen
            if (documentosImportantes != null && !documentosImportantes.isEmpty()) {
                for (MultipartFile doc : documentosImportantes) {
                    String ruta = guardarArchivo(doc, "uploads/documentos/");
                    DocumentoImportante documento = new DocumentoImportante();
                    documento.setRutaArchivo(ruta);
                    documento.setNombreDocumento(doc.getOriginalFilename());
                    documento.setFechaExpedicion(LocalDateTime.now());
                    documento.setInmueble(nuevoInmueble);
                    documentoImportanteRepository.save(documento);
                }
            }

            nuevoInmueble = inmuebleRepository.save(nuevoInmueble);
            return inmuebleMapper.toResponse(nuevoInmueble);


        } catch (Exception e) {
            throw new RuntimeException("Error al crear inmueble: " + e.getMessage(), e);
        }
    }

    @Override
    public InmuebleResponse crearInmueble(InmuebleDto inmuebleDto) {
        try{
            var nuevoInmueble = inmuebleMapper.toEntity(inmuebleDto);
            User agenteMenorCarga = buscarAgenteConMenorCarga();
            nuevoInmueble.setEstadoTransa(EstadoTransaccion.PENDIENTE);
            nuevoInmueble.setAgenteAsociado(agenteMenorCarga);
            nuevoInmueble.setAsesorLegal(agenteMenorCarga);
            inmuebleRepository.save(nuevoInmueble);
            return inmuebleMapper.toResponse(nuevoInmueble);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User buscarAgenteConMenorCarga() {
        int menor=0;
        User usuarioMenorCarga=null;
        List<User> users=userRepository.findByRol(Rol.AGENTE);
        for(int i=0;i<users.size();i++)
        {
            User user=users.get(i);
            if(i==0)
            {
                List<Inmueble> inmueblesAgente=inmuebleRepository.findByAgenteAsociado(user);
                menor=inmueblesAgente.size();
                usuarioMenorCarga=user;
            }
            else
            {
                List<Inmueble> inmueblesAgente=inmuebleRepository.findByAgenteAsociado(user);
                if(inmueblesAgente.size()<menor)
                {
                    menor=inmueblesAgente.size();
                    usuarioMenorCarga=user;
                }
            }
        }
        return usuarioMenorCarga;
    }

    @Override
    public User buscarAsesorConMenorCarga() {
        int menor=0;
        User usuarioMenorCarga=null;
        List<User> users=userRepository.findByRol(Rol.ASESOR_LEGAL);
        for(int i=0;i<users.size();i++)
        {
            User user=users.get(i);
            if(i==0)
            {
                List<Inmueble> inmueblesAsesor=inmuebleRepository.findByAsesorLegal(user);
                menor=inmueblesAsesor.size();
                usuarioMenorCarga=user;
            }
            else
            {
                List<Inmueble> inmueblesAgente=inmuebleRepository.findByAsesorLegal(user);
                if(inmueblesAgente.size()<menor)
                {
                    menor=inmueblesAgente.size();
                    usuarioMenorCarga=user;
                }
            }
        }
        return usuarioMenorCarga;
    }



    @Override
    public void eliminarInmueble(Long id) {
        try {
            var inmueble = inmuebleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Inmueble no encontrado con id: " + id));
            inmuebleRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el inmueble: " + e.getMessage(), e);
        }
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
                // Aquí deberías mapear los IDs a entidades Imagen si es necesario
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
