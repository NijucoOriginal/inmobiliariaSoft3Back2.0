package com.jsebastian.eden.EdenSys.repository;

import com.jsebastian.eden.EdenSys.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    /**
     * Busca un cliente por su documento de identidad
     * @param documentoIdentidad el documento de identidad del cliente
     * @return Optional<Cliente> el cliente encontrado o vacío si no existe
     */
    Optional<Cliente> findByDocumentoIdentidad(String documentoIdentidad);
    
    /**
     * Verifica si existe un cliente con el documento especificado
     * @param documentoIdentidad el documento a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByDocumentoIdentidad(String documentoIdentidad);
}