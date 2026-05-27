package com.aquacontrol.controller;

import com.aquacontrol.dto.ApiResponse;
import com.aquacontrol.entity.Usuario;
import com.aquacontrol.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('COMITE')")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Usuario>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.listarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.obtenerPorId(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Usuario>> crear(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Usuario creado", usuarioService.crear(usuario)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody Usuario usuario) {
        return ResponseEntity.ok(ApiResponse.ok("Usuario actualizado", usuarioService.actualizar(id, usuario)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<Usuario>> cambiarEstado(@PathVariable Long id,
                                                               @RequestBody Map<String, String> body) {
        Usuario.EstadoUsuario estado = Usuario.EstadoUsuario.valueOf(body.get("estado"));
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado", usuarioService.cambiarEstado(id, estado)));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<Void>> cambiarPassword(@PathVariable Long id,
                                                              @RequestBody Map<String, String> body) {
        usuarioService.cambiarPassword(id, body.get("password"));
        return ResponseEntity.ok(ApiResponse.ok("Contraseña actualizada", null));
    }
}
