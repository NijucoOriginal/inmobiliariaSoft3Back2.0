package com.jsebastian.eden.EdenSys.controller;

import com.jsebastian.eden.EdenSys.Dtos.RegisterRequest;
import com.jsebastian.eden.EdenSys.Dtos.LoginRequest;
import com.jsebastian.eden.EdenSys.Dtos.AuthResponse;
import com.jsebastian.eden.EdenSys.services.UserService;
import com.jsebastian.eden.EdenSys.services.JwtService;
import com.jsebastian.eden.EdenSys.exceptions.ValueConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("https://singular-belekoy-a36f07.netlify.app")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            userService.crearUsuario(request.toCrearUsuarioDto());
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente. Por favor, verifica tu correo para activar tu cuenta.");
        } catch (ValueConflictException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/activate/{codigo}")
    public ResponseEntity<String> activate(@PathVariable String codigo) {
        boolean activated = userService.activarUsuario(codigo);
        if (activated) {
            return ResponseEntity.ok("Cuenta activada exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código de activación inválido o expirado.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            String token = userService.validarCredencialesYGenerarToken(request.email(), request.contrasena());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(e.getMessage()));
        }
    }
}
