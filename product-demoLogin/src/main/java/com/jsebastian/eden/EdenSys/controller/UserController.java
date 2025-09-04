package com.jsebastian.eden.EdenSys.controller;

import com.jsebastian.eden.EdenSys.domain.User;
import com.jsebastian.eden.EdenSys.services.UserService;
import com.jsebastian.eden.EdenSys.Dtos.CrearUsuarioDto;
import com.jsebastian.eden.EdenSys.exceptions.ValueConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar operaciones CRUD de usuarios
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Crea un nuevo usuario usando DTO con validaciones Jakarta
     * @param crearUsuarioDto el DTO con los datos del usuario a crear
     * @return ResponseEntity con el usuario creado
     */
    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody CrearUsuarioDto crearUsuarioDto) {
        try {
            User usuarioCreado = userService.crearUsuario(crearUsuarioDto);
            return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED);
        } catch (ValueConflictException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<User> actualizarUsuario(@PathVariable Long id, @RequestBody User user) {
        try {
            user.setId(id);
            User usuarioActualizado = userService.actualizarUsuario(user);
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
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
