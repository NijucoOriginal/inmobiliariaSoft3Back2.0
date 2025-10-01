package com.jsebastian.eden.EdenSys.controller;

import com.jsebastian.eden.EdenSys.Dtos.UsuarioResponse;
import com.jsebastian.eden.EdenSys.domain.User;
import com.jsebastian.eden.EdenSys.services.UserService;
import com.jsebastian.eden.EdenSys.Dtos.CrearUsuarioDto;
import com.jsebastian.eden.EdenSys.exceptions.ValueConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para gestionar operaciones CRUD de usuarios
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("https://singular-belekoy-a36f07.netlify.app")
//@CrossOrigin("http://localhost:4200/")
public class UserController {


    private final UserService userService;

    /**
     * Crea un nuevo usuario usando DTO con validaciones Jakarta
     * @param crearUsuarioDto el DTO con los datos del usuario a crear
     * @return ResponseEntity con el usuario creado
     */
    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody CrearUsuarioDto crearUsuarioDto) {
        try {
            UsuarioResponse response = userService.crearUsuario(crearUsuarioDto);
            return ResponseEntity.ok(response);
        } catch (ValueConflictException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Conflicto de datos");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno del servidor");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error inesperado");
            errorResponse.put("message", "Ocurrió un error al procesar la solicitud");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    /**
     * Obtiene todos los usuarios
     * @return ResponseEntity con la lista de usuarios
     */
    @GetMapping
    public ResponseEntity<List<User>> obtenerTodosLosUsuarios() {
        List<User> usuarios = userService.obtenerTodosLosUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    /**
     * Obtiene un usuario por su ID
     * @param id el ID del usuario
     * @return ResponseEntity con el usuario encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<User> usuario = userService.buscarPorId(id);
        return usuario.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Obtiene un usuario por su email
     * @param email el email del usuario
     * @return ResponseEntity con el usuario encontrado
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> obtenerUsuarioPorEmail(@PathVariable String email) {
        Optional<User> usuario = userService.buscarPorEmail(email);
        return usuario.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Obtiene un usuario por su cédula
     * @param cedula la cédula del usuario
     * @return ResponseEntity con el usuario encontrado
     */
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<User> obtenerUsuarioPorCedula(@PathVariable String cedula) {
        Optional<User> usuario = userService.buscarPorCedula(cedula);
        return usuario.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Actualiza un usuario existente
     * @param id el ID del usuario a actualizar
     * @param user los nuevos datos del usuario
     * @return ResponseEntity con el usuario actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable String id, @RequestBody CrearUsuarioDto user) {
        try {
            Optional<UsuarioResponse>usuarioActualizado = userService.actualizarUsuario(id,user);
            return usuarioActualizado.map(ResponseEntity::ok)
                    .orElseGet(()-> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina un usuario por su ID
     * @param id el ID del usuario a eliminar
     * @return ResponseEntity sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            userService.eliminarUsuario(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina un usuario por su email
     * @param email el email del usuario a eliminar
     * @return ResponseEntity sin contenido
     */
    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> eliminarUsuarioPorEmail(@PathVariable String email) {
        try {
            userService.eliminarUsuarioPorEmail(email);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Verifica si existe un usuario con el email especificado
     * @param email el email a verificar
     * @return ResponseEntity con true si existe, false en caso contrario
     */
    @GetMapping("/existe/email/{email}")
    public ResponseEntity<Boolean> existeUsuarioPorEmail(@PathVariable String email) {
        boolean existe = userService.existePorEmail(email);
        return new ResponseEntity<>(existe, HttpStatus.OK);
    }

    /**
     * Verifica si existe un usuario con la cédula especificada
     * @param cedula la cédula a verificar
     * @return ResponseEntity con true si existe, false en caso contrario
     */
    @GetMapping("/existe/cedula/{cedula}")
    public ResponseEntity<Boolean> existeUsuarioPorCedula(@PathVariable String cedula) {
        boolean existe = userService.existePorCedula(cedula);
        return new ResponseEntity<>(existe, HttpStatus.OK);
    }
}