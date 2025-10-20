package com.jsebastian.eden.EdenSys.mappers;

import com.jsebastian.eden.EdenSys.domain.Inmueble;
import com.jsebastian.eden.EdenSys.Dtos.InmuebleDto;
import com.jsebastian.eden.EdenSys.Dtos.InmuebleResponse;
import com.jsebastian.eden.EdenSys.domain.Imagen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InmuebleMapper {
    @Mapping(target = "departamento", source = "departamento")
    @Mapping(target = "ciudad", source = "ciudad")
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "tipoNegocio", source = "tipoNegocio")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "medidas", source = "medidas")
    @Mapping(target = "habitaciones", source = "habitaciones")
    @Mapping(target = "banos", source = "banos")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "precio", source = "precio")
    @Mapping(target = "cantidadParqueaderos", source = "cantidadParqueaderos")
    @Mapping(target = "telfonoContacto", source = "telfonoContacto")
    @Mapping(target = "nombreContacto", source = "nombreContacto")
    @Mapping(target = "correoContacto", source = "correoContacto")
    @Mapping(target = "imagenes", source = "imagenes", qualifiedByName = "stringListToImagenList")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "documentosImportantes", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "estadoPosteoInmueble", ignore = true)
    @Mapping(target = "estadoTransa", ignore = true)
    @Mapping(target = "codigoInmueble", ignore = true)
    @Mapping(target = "historial", ignore = true)
    @Mapping(target = "asesorLegal", ignore = true)
    @Mapping(target = "agenteAsociado", ignore = true)
    @Mapping(target = "propietario", ignore = true)
    Inmueble toEntity(InmuebleDto dto);

    @Mapping(target = "departamento", source = "departamento")
    @Mapping(target = "ciudad", source = "ciudad")
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "tipoNegocio", source = "tipoNegocio")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "medidas", source = "medidas")
    @Mapping(target = "habitaciones", source = "habitaciones")
    @Mapping(target = "banos", source = "banos")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "precio", source = "precio")
    @Mapping(target = "cantidadParqueaderos", source = "cantidadParqueaderos")
    @Mapping(target = "telfonoContacto", source = "telfonoContacto")
    @Mapping(target = "nombreContacto", source = "nombreContacto")
    @Mapping(target = "correoContacto", source = "correoContacto")
    @Mapping(target = "imagenes", source = "imagenes")
    InmuebleResponse toResponse(Inmueble entity);

    // Método utilitario para actualizar los campos de una entidad Inmueble existente con los valores de un InmuebleDto.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagenes", ignore = true)
    void updateEntityFromDto(InmuebleDto dto, @MappingTarget Inmueble entity);

    // Mapeo de lista de URLs a lista de Imagenes
    @Named("stringListToImagenList")
    default List<Imagen> stringListToImagenList(List<String> urls) {
        if (urls == null) return new ArrayList<>();
        return urls.stream()
                .map(url -> {
                    Imagen imagen = new Imagen();
                    imagen.setUrl(url);
                    imagen.setNombre(extraerNombreDeUrl(url));
                    imagen.setCodigoImagen(UUID.randomUUID().toString());
                    // La referencia al inmueble se establecerá después de crear la entidad
                    return imagen;
                })
                .toList();
    }

    // Mapeo de lista de Imagenes a lista de URLs
    @Named("imagenListToStringList")
    default List<String> imagenListToStringList(List<Imagen> imagenes) {
        if (imagenes == null) return new ArrayList<>();
        return imagenes.stream()
                .map(Imagen::getUrl)
                .toList();
    }

    // Utilidad para obtener el nombre del archivo a partir de la URL
    @Named("extraerNombreDeUrl")
    default String extraerNombreDeUrl(String url) {
        if (url == null || !url.contains("/")) return "imagen_sin_nombre";
        return url.substring(url.lastIndexOf('/') + 1);
    }
}