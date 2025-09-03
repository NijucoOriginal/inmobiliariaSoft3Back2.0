package com.jsebastian.eden.EdenSys.domain;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@NotEmpty
@NotBlank
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoImportante {

    @PastOrPresent
    private LocalDateTime fechaExpedicion;

    private String descripcion;

    private int id;

    private String nombreDocumento;
    
}
