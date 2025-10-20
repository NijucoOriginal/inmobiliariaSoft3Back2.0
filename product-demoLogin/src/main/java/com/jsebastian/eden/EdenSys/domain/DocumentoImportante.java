package com.jsebastian.eden.EdenSys.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NotEmpty
@NotBlank
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoImportante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @PastOrPresent
    private LocalDateTime fechaExpedicion;
    private String descripcion;
    private String nombreDocumento;
    
    @ManyToOne
    @JoinColumn(name = "inmueble_id")
    private Inmueble inmueble;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;


}