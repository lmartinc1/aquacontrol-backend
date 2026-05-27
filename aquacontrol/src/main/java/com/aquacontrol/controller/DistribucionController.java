package com.aquacontrol.controller;

import com.aquacontrol.dto.ApiResponse;
import com.aquacontrol.entity.Distribucion;
import com.aquacontrol.service.DistribucionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/distribucion")
public class DistribucionController {

    @Autowired
    private DistribucionService distribucionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('COMITE','REPRESENTANTE')")
    public ResponseEntity<ApiResponse<List<Distribucion>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.ok(distribucionService.listarTodos()));
    }

    @GetMapping("/{sector}")
    @PreAuthorize("hasAnyRole('COMITE','REPRESENTANTE')")
    public ResponseEntity<ApiResponse<List<Distribucion>>> listarPorSector(@PathVariable String sector) {
        return ResponseEntity.ok(ApiResponse.ok(distribucionService.listarPorSector(sector)));
    }

    @PostMapping
    @PreAuthorize("hasRole('COMITE')")
    public ResponseEntity<ApiResponse<Distribucion>> programar(@Valid @RequestBody Distribucion distribucion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Distribución programada", distribucionService.programar(distribucion)));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('COMITE')")
    public ResponseEntity<ApiResponse<Distribucion>> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Distribucion.EstadoDistribucion estado = Distribucion.EstadoDistribucion.valueOf(body.get("estado"));
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado",
                distribucionService.actualizarEstado(id, estado)));
    }
}
