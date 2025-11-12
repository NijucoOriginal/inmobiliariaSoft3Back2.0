package com.jsebastian.eden.EdenSys.services.interfaces;

import com.jsebastian.eden.EdenSys.Dtos.*;
import java.util.List;

public interface NegociacionInmuebleService {
    List<InmuebleAsignadoResponse> obtenerInmueblesAsignados(Long agenteId);
    CambioEstadoResponse cambiarEstadoInmueble(Long inmuebleId, CambiarEstadoInmuebleRequest dto, Long agenteId);
    ClienteAsociadoResponse obtenerClienteAsociado(Long inmuebleId);
}

