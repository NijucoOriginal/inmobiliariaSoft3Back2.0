package com.jsebastian.eden.EdenSys.mappers;

import com.jsebastian.eden.EdenSys.domain.Inmueble;
import com.jsebastian.eden.EdenSys.Dtos.InmuebleDto;
import com.jsebastian.eden.EdenSys.Dtos.InmuebleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InmuebleMapper {
    //@Mapping(target = "departamento", source = "departamento")
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "tipoNegocio", source = "tipoNegocio")
    //@Mapping(target = "agenteAsociado", source = "agenteAsociado")
    @Mapping(target = "documentosImportantes", source = "documentosImportantes")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "medidas", source = "medidas")
    @Mapping(target = "habitaciones", source = "habitaciones")
    @Mapping(target = "banos", source = "banos")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "precio", source = "precio")
    @Mapping(target = "estadoTransa", source = "estadoTransa")
    @Mapping(target = "ciudad", source = "ciudad")
    @Mapping(target = "codigoInmueble", source = "codigoInmueble")
    @Mapping(target = "historial", source = "historial")
    @Mapping(target = "asesorLegal", source = "asesorLegal")
    @Mapping(target = "cantidadParqueaderos", source = "cantidadParqueaderos")
    @Mapping(target = "telfonoContacto", source = "telfonoContacto")
    @Mapping(target = "nombreContacto", source = "nombreContacto")
    @Mapping(target = "correoContacto", source = "correoContacto")
    @Mapping(target = "imagenes", source = "imagenes")
    Inmueble toEntity(InmuebleDto dto);

    //@Mapping(target = "departamento", source = "departamento")
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "tipoNegocio", source = "tipoNegocio")
    @Mapping(target = "agenteAsociado", source = "agenteAsociado")
    @Mapping(target = "documentosImportantes", source = "documentosImportantes")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "medidas", source = "medidas")
    @Mapping(target = "habitaciones", source = "habitaciones")
    @Mapping(target = "banos", source = "banos")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "precio", source = "precio")
    @Mapping(target = "estadoTransa", source = "estadoTransa")
    @Mapping(target = "ciudad", source = "ciudad")
    @Mapping(target = "codigoInmueble", source = "codigoInmueble")
    @Mapping(target = "historial", source = "historial")
    //@Mapping(target = "asesorLegal", source = "asesorLegal") Por regla de negocio el asesor legal no se asigna en la creacion
    @Mapping(target = "cantidadParqueaderos", source = "cantidadParqueaderos")
    @Mapping(target = "telfonoContacto", source = "telfonoContacto")
    @Mapping(target = "nombreContacto", source = "nombreContacto")
    @Mapping(target = "correoContacto", source = "correoContacto")
    @Mapping(target = "imagenes", source = "imagenes")
    InmuebleResponse toResponse(Inmueble entity);

    // Método utilitario para actualizar los campos de una entidad Inmueble existente con los valores de un InmuebleDto.
    void updateEntityFromDto(InmuebleDto dto, @org.mapstruct.MappingTarget com.jsebastian.eden.EdenSys.domain.Inmueble entity);
}
