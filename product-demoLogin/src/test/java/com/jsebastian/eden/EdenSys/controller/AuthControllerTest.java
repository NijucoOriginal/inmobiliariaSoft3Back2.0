package com.jsebastian.eden.EdenSys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsebastian.eden.EdenSys.Dtos.AuthResponse;
import com.jsebastian.eden.EdenSys.Dtos.LoginRequest;
import com.jsebastian.eden.EdenSys.Dtos.RegisterRequest;
import com.jsebastian.eden.EdenSys.exceptions.ValueConflictException;
import com.jsebastian.eden.EdenSys.services.JwtService;
import com.jsebastian.eden.EdenSys.services.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void register_DebeRetornar201_CuandoExitoso() throws Exception {
        RegisterRequest req = new RegisterRequest("Juan","Pérez","12345678","3001234567","test@example.com","Password123@");
        String json = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
        System.out.println("[LOG] Registro exitoso para: " + req.email());
    }

    @Test
    void register_DebeRetornar400_CuandoEmailDuplicado() throws Exception {
        RegisterRequest req = new RegisterRequest("Juan","Pérez","12345678","3001234567","test@example.com","Password123@");
        when(userService.crearUsuario(any())).thenThrow(new ValueConflictException("duplicado"));
        String json = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void activate_DebeRetornar200_CuandoCodigoValido() throws Exception {
        when(userService.activarUsuario("ABC123")).thenReturn(true);
        mockMvc.perform(get("/api/auth/activate/ABC123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("activada")));
        System.out.println("[LOG] Activación exitosa para código: ABC123");
    }

    @Test
    void activate_DebeRetornar400_CuandoCodigoInvalido() throws Exception {
        when(userService.activarUsuario("XYZ")).thenReturn(false);
        mockMvc.perform(get("/api/auth/activate/XYZ"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_DebeRetornarToken_CuandoCredencialesValidas() throws Exception {
        LoginRequest req = new LoginRequest("test@example.com","Password123@");
        when(userService.validarCredencialesYGenerarToken(eq("test@example.com"), eq("Password123@")))
                .thenReturn("jwt.token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt.token"));
        System.out.println("[LOG] Login exitoso para: " + req.email() + " | Token: jwt.token");
    }

    @Test
    void login_DebeRetornar401_CuandoCredencialesInvalidas() throws Exception {
        LoginRequest req = new LoginRequest("test@example.com","wrong");
        when(userService.validarCredencialesYGenerarToken(eq("test@example.com"), eq("wrong")))
                .thenThrow(new IllegalArgumentException("Credenciales invalidas"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").value("Credenciales invalidas"));
    }
}
