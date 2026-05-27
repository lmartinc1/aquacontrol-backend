package com.aquacontrol.controller;

import com.aquacontrol.dto.ApiResponse;
import com.aquacontrol.entity.Aviso;
import com.aquacontrol.service.AvisoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avisos")
public class AvisoController {

    @Autowired
    private AvisoService avisoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('COMITE','REPRESENTANTE')")
    public ResponseEntity<ApiResponse<List<Aviso>>> listarVigentes() {
        return ResponseEntity.ok(ApiResponse.ok(avisoService.listarTodosVigentes()));
    }

    @GetMapping("/todos")
    @PreAuthorize("hasRole('COMITE')")
    public ResponseEntity<ApiResponse<List<Aviso>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.ok(avisoService.listarTodos()));
    }

    @GetMapping("/sector/{sector}")
    @PreAuthorize("hasAnyRole('COMITE','REPRESENTANTE')")
    public ResponseEntity<ApiResponse<List<Aviso>>> listarPorSector(@PathVariable String sector) {
        return ResponseEntity.ok(ApiResponse.ok(avisoService.listarVigentesPorSector(sector)));
    }

    @PostMapping
    @PreAuthorize("hasRole('COMITE')")
    public ResponseEntity<ApiResponse<Aviso>> registrar(@Valid @RequestBody Aviso aviso) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Aviso publicado", avisoService.registrar(aviso)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMITE')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        avisoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Aviso eliminado", null));
    }
}
