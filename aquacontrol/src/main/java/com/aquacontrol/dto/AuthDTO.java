package com.aquacontrol.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class AuthDTO {

    @Data
    public static class LoginRequest {
        @NotBlank @Email
        private String email;
        @NotBlank
        private String password;
    }

    @Data
    public static class JwtResponse {
        private String token;
        private String tipo = "Bearer";
        private Long idUsuario;
        private String nombre;
        private String email;
        private String rol;

        public JwtResponse(String token, Long idUsuario, String nombre, String email, String rol) {
            this.token = token;
            this.idUsuario = idUsuario;
            this.nombre = nombre;
            this.email = email;
            this.rol = rol;
        }
    }
}
