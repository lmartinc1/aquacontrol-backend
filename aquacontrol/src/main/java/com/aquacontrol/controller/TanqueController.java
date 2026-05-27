package com.aquacontrol.controller;

import com.aquacontrol.dto.ApiResponse;
import com.aquacontrol.entity.Tanque;
import com.aquacontrol.service.TanqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tanque")
public class TanqueController {

    @Autowired
    private TanqueService tanqueService;

    @GetMapping
    @PreAuthorize("hasAnyRole('COMITE','TECNICO')")
    public ResponseEntity<ApiResponse<List<Tanque>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(tanqueService.listarTodos()));
    }

    @GetMapping("/ultimo")
    @PreAuthorize("hasAnyRole('COMITE','TECNICO','REPRESENTANTE')")
    public ResponseEntity<ApiResponse<Tanque>> ultimo() {
        return ResponseEntity.ok(ApiResponse.ok(tanqueService.obtenerUltimo()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('COMITE','TECNICO')")
    public ResponseEntity<ApiResponse<Tanque>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(tanqueService.obtenerPorId(id)));
    }

    // RF-15-CU: Registrar nivel del tanque manualmente
    @PostMapping
    @PreAuthorize("hasRole('COMITE')")
    public ResponseEntity<ApiResponse<Tanque>> registrar(@Valid @RequestBody Tanque tanque) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Nivel del tanque registrado", tanqueService.registrarNivel(tanque)));
    }
}
