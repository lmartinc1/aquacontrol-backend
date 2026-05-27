package com.aquacontrol.controller;

import com.aquacontrol.dto.ApiResponse;
import com.aquacontrol.entity.Problema;
import com.aquacontrol.service.ProblemaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/problemas")
public class ProblemaController {

    @Autowired
    private ProblemaService problemaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('COMITE','TECNICO')")
    public ResponseEntity<ApiResponse<Page<Problema>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Problema.EstadoProblema estado) {
        if (estado != null) {
            return ResponseEntity.ok(ApiResponse.ok(null)); // filtrar por estado
        }
        return ResponseEntity.ok(ApiResponse.ok(problemaService.listarTodos(PageRequest.of(page, size))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('COMITE','TECNICO')")
    public ResponseEntity<ApiResponse<Problema>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(problemaService.obtenerPorId(id)));
    }

    @GetMapping("/hogar/{idHogar}")
    @PreAuthorize("hasAnyRole('COMITE','TECNICO','REPRESENTANTE')")
    public ResponseEntity<ApiResponse<List<Problema>>> listarPorHogar(@PathVariable Long idHogar) {
        return ResponseEntity.ok(ApiResponse.ok(problemaService.listarPorHogar(idHogar)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMITE','REPRESENTANTE')")
    public ResponseEntity<ApiResponse<Problema>> registrar(@Valid @RequestBody Problema problema) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Problema registrado", problemaService.registrar(problema)));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('COMITE','TECNICO')")
    public ResponseEntity<ApiResponse<Problema>> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Problema.EstadoProblema nuevoEstado = Problema.EstadoProblema.valueOf(body.get("estado"));
        String observacion = body.get("observacion");
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado",
                problemaService.actualizarEstado(id, nuevoEstado, observacion)));
    }
}
