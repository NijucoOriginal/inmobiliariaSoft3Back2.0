package com.jsebastian.eden.EdenSys.mappers;

import com.jsebastian.eden.EdenSys.domain.User;
import com.jsebastian.eden.EdenSys.Dtos.CrearUsuarioDto;
import org.springframework.stereotype.Component;

/**
 * Mapper para transformar entre DTOs y entidades de User
 */
@Component
public class UserMapper {

    /**
     * Convierte un CrearUsuarioDto a una entidad User
     * @param dto el DTO con los datos del usuario
     * @return la entidad User creada
     */
    public User toEntity(CrearUsuarioDto dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setContrasena(dto.getContrasena());
        // El rol se establece automáticamente como USER por defecto desde Persona
        
        return user;
    }

    /**
     * Convierte una entidad User a un CrearUsuarioDto
     * @param user la entidad User
     * @return el DTO con los datos del usuario
     */
    public CrearUsuarioDto toDto(User user) {
        if (user == null) {
            return null;
        }
        
        CrearUsuarioDto dto = new CrearUsuarioDto();
        dto.setEmail(user.getEmail());
        dto.setContrasena(user.getContrasena());
        
        return dto;
    }
}
