package com.aquacontrol.controller;

import com.aquacontrol.dto.ApiResponse;
import com.aquacontrol.entity.Mantenimiento;
import com.aquacontrol.service.MantenimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mantenimiento")
public class MantenimientoController {

    @Autowired
    private MantenimientoService mantenimientoService;

    @GetMapping("/problema/{idProblema}")
    @PreAuthorize("hasAnyRole('COMITE','TECNICO')")
    public ResponseEntity<ApiResponse<List<Mantenimiento>>> listarPorProblema(@PathVariable Long idProblema) {
        return ResponseEntity.ok(ApiResponse.ok(mantenimientoService.listarPorProblema(idProblema)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('COMITE','TECNICO')")
    public ResponseEntity<ApiResponse<Mantenimiento>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(mantenimientoService.obtenerPorId(id)));
    }

    // RF-10: Técnico registra mantenimiento físico
    // RF-12: Comité registra actividad administrativa
    @PostMapping
    @PreAuthorize("hasAnyRole('COMITE','TECNICO')")
    public ResponseEntity<ApiResponse<Mantenimiento>> registrar(@Valid @RequestBody Mantenimiento mantenimiento) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Mantenimiento registrado", mantenimientoService.registrar(mantenimiento)));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('COMITE','TECNICO')")
    public ResponseEntity<ApiResponse<Mantenimiento>> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Mantenimiento.EstadoMantenimiento estado = Mantenimiento.EstadoMantenimiento.valueOf(body.get("estado"));
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado",
                mantenimientoService.actualizarEstado(id, estado)));
    }
}
