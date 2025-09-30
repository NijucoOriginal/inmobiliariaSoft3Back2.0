package com.jsebastian.eden.EdenSys.services.interfaces;

import com.jsebastian.eden.EdenSys.Dtos.InmuebleDto;
import com.jsebastian.eden.EdenSys.Dtos.InmueblePatchDto;
import com.jsebastian.eden.EdenSys.Dtos.InmuebleResponse;
import com.jsebastian.eden.EdenSys.exceptions.ValueConflictException;

import java.util.List;

public interface InmuebleService {
    InmuebleResponse crearInmueble(InmuebleDto inmuebleDto) throws ValueConflictException;
    void eliminarInmueble(Long id);
    InmuebleResponse actualizarInmueble(Long id, InmuebleDto inmuebleDto);
    InmuebleResponse patchInmueble(Long id, InmueblePatchDto patchDto);
    InmuebleResponse obtenerInmueble(Long id);
    List<InmuebleResponse> obtenerListaDeInmuebles();
}
