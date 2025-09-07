package com.jsebastian.eden.EdenSys.Dtos;

/**
 * DTO para la respuesta de autenticación que contiene el token JWT
 * @param token El token JWT generado tras la autenticación exitosa
 */
public record AuthResponse(String token) {
}
