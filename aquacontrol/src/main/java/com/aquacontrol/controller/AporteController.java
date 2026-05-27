package com.aquacontrol.controller;

import com.aquacontrol.dto.ApiResponse;
import com.aquacontrol.entity.Aporte;
import com.aquacontrol.service.AporteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/aportes")
public class AporteController {

    @Autowired
    private AporteService aporteService;

    // POST /api/aportes
    @PostMapping
    @PreAuthorize("hasRole('COMITE')")
    public ResponseEntity<ApiResponse<Aporte>> registrar(@Valid @RequestBody Aporte aporte) {
        Aporte nuevo = aporteService.registrar(aporte);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Aporte registrado exitosamente", nuevo));
    }

    // GET /api/aportes/{idHogar}?page=0&size=10
    @GetMapping("/{idHogar}")
    @PreAuthorize("hasAnyRole('COMITE','REPRESENTANTE')")
    public ResponseEntity<ApiResponse<Page<Aporte>>> listarPorHogar(
            @PathVariable Long idHogar,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Aporte> resultado = aporteService.listarPorHogar(idHogar, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.ok(resultado));
    }

    // GET /api/aportes/{idHogar}/historial?inicio=2026-01-01&fin=2026-12-31
    @GetMapping("/{idHogar}/historial")
    @PreAuthorize("hasAnyRole('COMITE','REPRESENTANTE')")
    public ResponseEntity<ApiResponse<List<Aporte>>> historial(
            @PathVariable Long idHogar,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin) {
        List<Aporte> resultado = aporteService.listarPorHogarYFecha(idHogar, inicio, fin);
        return ResponseEntity.ok(ApiResponse.ok(resultado));
    }
}
