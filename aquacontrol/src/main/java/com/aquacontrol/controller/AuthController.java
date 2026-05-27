package com.aquacontrol.controller;

import com.aquacontrol.dto.ApiResponse;
import com.aquacontrol.dto.AuthDTO;
import com.aquacontrol.entity.Usuario;
import com.aquacontrol.repository.UsuarioRepository;
import com.aquacontrol.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDTO.LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateToken(request.getEmail(),
                    authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", ""));

            Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

            AuthDTO.JwtResponse response = new AuthDTO.JwtResponse(
                    jwt,
                    usuario.getIdUsuario(),
                    usuario.getNombre(),
                    usuario.getEmail(),
                    usuario.getRol().name()
            );

            return ResponseEntity.ok(ApiResponse.ok("Inicio de sesión exitoso", response));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(ApiResponse.error("Credenciales incorrectas"));
        } catch (DisabledException e) {
            return ResponseEntity.status(403).body(ApiResponse.error("Usuario inactivo"));
        }
    }
}
