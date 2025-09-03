package com.jsebastian.eden.EdenSys.services;

import com.jsebastian.eden.EdenSys.domain.User;
import com.jsebastian.eden.EdenSys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones relacionadas con usuarios
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Guarda un nuevo usuario en la base de datos
     * @param user el usuario a guardar
     * @return el usuario guardado con su ID generado
     */
    public User guardarUsuario(User user) {
        return userRepository.save(user);
    }

    /**
     * Busca un usuario por su email
     * @param email el email del usuario a buscar
     * @return Optional<User> el usuario encontrado o vacío si no existe
     */
    public Optional<User> buscarPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Busca un usuario por su ID
     * @param id el ID del usuario a buscar
     * @return Optional<User> el usuario encontrado o vacío si no existe
     */
    public Optional<User> buscarPorId(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Busca un usuario por su cédula
     * @param cedula la cédula del usuario a buscar
     * @return Optional<User> el usuario encontrado o vacío si no existe
     */
    public Optional<User> buscarPorCedula(String cedula) {
        return userRepository.findByCedula(cedula);
    }

    /**
     * Obtiene todos los usuarios
     * @return List<User> lista de todos los usuarios
     */
    public List<User> obtenerTodosLosUsuarios() {
        return userRepository.findAll();
    }

    /**
     * Elimina un usuario por su ID
     * @param id el ID del usuario a eliminar
     */
    public void eliminarUsuario(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Elimina un usuario por su email
     * @param email el email del usuario a eliminar
     */
    public void eliminarUsuarioPorEmail(String email) {
        Optional<User> usuario = userRepository.findByEmail(email);
        if (usuario.isPresent()) {
            userRepository.delete(usuario.get());
        }
    }

    /**
     * Verifica si existe un usuario con el email especificado
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existePorEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Verifica si existe un usuario con la cédula especificada
     * @param cedula la cédula a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existePorCedula(String cedula) {
        return userRepository.existsByCedula(cedula);
    }

    /**
     * Actualiza un usuario existente
     * @param user el usuario con los datos actualizados
     * @return el usuario actualizado
     */
    public User actualizarUsuario(User user) {
        if (user.getId() != null && userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        }
        throw new IllegalArgumentException("El usuario no existe o no tiene ID válido");
    }
}
