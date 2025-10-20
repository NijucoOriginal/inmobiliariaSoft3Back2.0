package com.jsebastian.eden.EdenSys.controller;

import com.jsebastian.eden.EdenSys.Dtos.InmuebleDto;
import com.jsebastian.eden.EdenSys.Dtos.InmueblePatchDto;
import com.jsebastian.eden.EdenSys.Dtos.InmuebleResponse;
import com.jsebastian.eden.EdenSys.exceptions.ValueConflictException;
import com.jsebastian.eden.EdenSys.services.interfaces.InmuebleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inmuebles")
public class InmuebleController {
    private final InmuebleService inmuebleService;
    /*@PostMapping
    public ResponseEntity<?> crearInmueble(@Valid @RequestBody InmuebleDto inmuebleDto) {
        try {
            InmuebleResponse inmuebleResponse = inmuebleService.crearInmueble(inmuebleDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(inmuebleResponse);
        } catch (ValueConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        }
    }

     */

    @PostMapping
    public ResponseEntity<?> crearInmueble(
            @RequestPart("inmuebleDto") @Valid InmuebleDto inmuebleDto,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes,
            @RequestPart(value = "documentosImportantes", required = false) List<MultipartFile> documentosImportantes,
            @RequestParam("correoUsuario") String correoUsuario
    ) {
        try
        {
            // Lógica del servicio: guardar archivos, asociarlos al inmueble, etc.
            InmuebleResponse inmuebleResponse = inmuebleService.crearInmueble(inmuebleDto, imagenes, documentosImportantes,correoUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(inmuebleResponse);
        }
        catch (ValueConflictException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInmueble(@PathVariable Long id) {
        try {
            InmuebleResponse response = inmuebleService.obtenerInmueble(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<InmuebleResponse>> obtenerListaDeInmuebles() {
        List<InmuebleResponse> lista = inmuebleService.obtenerListaDeInmuebles();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInmueble(@PathVariable Long id, @Valid @RequestBody InmuebleDto inmuebleDto) {
        try {
            InmuebleResponse response = inmuebleService.actualizarInmueble(id, inmuebleDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchInmueble(@PathVariable Long id, @RequestBody InmueblePatchDto patchDto) {
        try {
            InmuebleResponse response = inmuebleService.patchInmueble(id, patchDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarInmueble(@PathVariable Long id) {
        try {
            inmuebleService.eliminarInmueble(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
