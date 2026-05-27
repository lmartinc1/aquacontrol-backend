package com.aquacontrol.controller;

import com.aquacontrol.dto.ApiResponse;
import com.aquacontrol.entity.Hogar;
import com.aquacontrol.service.HogarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hogares")
public class HogarController {

    @Autowired
    private HogarService hogarService;

    // GET /api/hogares?page=0&size=10
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Hogar>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.ok(hogarService.listarTodos(pageable)));
    }

    // GET /api/hogares/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Hogar>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(hogarService.obtenerPorId(id)));
    }

    // GET /api/hogares/sector/{sector}
    @GetMapping("/sector/{sector}")
    public ResponseEntity<ApiResponse<List<Hogar>>> listarPorSector(@PathVariable String sector) {
        return ResponseEntity.ok(ApiResponse.ok(hogarService.listarPorSector(sector)));
    }

    // POST /api/hogares
    @PostMapping
    @PreAuthorize("hasRole('COMITE')")
    public ResponseEntity<ApiResponse<Hogar>> registrar(@Valid @RequestBody Hogar hogar) {
        Hogar nuevo = hogarService.registrar(hogar);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Hogar registrado exitosamente", nuevo));
    }

    // PUT /api/hogares/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMITE')")
    public ResponseEntity<ApiResponse<Hogar>> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody Hogar hogar) {
        return ResponseEntity.ok(ApiResponse.ok("Hogar actualizado", hogarService.actualizar(id, hogar)));
    }

    // PATCH /api/hogares/{id}/estado
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('COMITE')")
    public ResponseEntity<ApiResponse<Hogar>> cambiarEstado(@PathVariable Long id,
                                                              @RequestParam Hogar.EstadoHogar estado) {
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado", hogarService.cambiarEstado(id, estado)));
    }

    // DELETE /api/hogares/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMITE')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        hogarService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Hogar eliminado", null));
    }
}
