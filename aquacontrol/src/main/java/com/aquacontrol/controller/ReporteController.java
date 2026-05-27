package com.aquacontrol.controller;

import com.aquacontrol.dto.ApiResponse;
import com.aquacontrol.entity.*;
import com.aquacontrol.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@PreAuthorize("hasRole('COMITE')")
public class ReporteController {

    @Autowired private HogarRepository hogarRepository;
    @Autowired private AporteRepository aporteRepository;
    @Autowired private ProblemaRepository problemaRepository;
    @Autowired private MantenimientoRepository mantenimientoRepository;
    @Autowired private TanqueRepository tanqueRepository;

    // GET /api/reportes/resumen — Panel principal del comité
    @GetMapping("/resumen")
    public ResponseEntity<ApiResponse<Map<String, Object>>> resumenGeneral() {
        Map<String, Object> resumen = new HashMap<>();

        // Hogares
        resumen.put("totalHogares", hogarRepository.count());
        resumen.put("hogaresActivos", hogarRepository.findByEstado(Hogar.EstadoHogar.ACTIVO).size());
        resumen.put("hogareMorosos", hogarRepository.findByEstado(Hogar.EstadoHogar.MOROSO).size());
        resumen.put("hogaresSuspendidos", hogarRepository.findByEstado(Hogar.EstadoHogar.SUSPENDIDO).size());

        // Problemas
        resumen.put("problemasPendientes", problemaRepository.findByEstado(Problema.EstadoProblema.PENDIENTE).size());
        resumen.put("problemasEnProceso", problemaRepository.findByEstado(Problema.EstadoProblema.EN_PROCESO).size());
        resumen.put("problemasResueltos", problemaRepository.findByEstado(Problema.EstadoProblema.RESUELTO).size());

        // Tanque último nivel
        tanqueRepository.findUltimoRegistro().ifPresent(t -> {
            resumen.put("nivelTanque", t.getNivelActual());
            resumen.put("capacidadTanque", t.getCapacidad());
            resumen.put("porcentajeTanque", t.getPorcentajeNivel());
            resumen.put("nivelCritico", t.isNivelCritico());
            resumen.put("fechaUltimoRegistroTanque", t.getFecha());
        });

        return ResponseEntity.ok(ApiResponse.ok("Resumen general", resumen));
    }

    // GET /api/reportes/aportes — Reporte de aportes
    @GetMapping("/aportes")
    public ResponseEntity<ApiResponse<Map<String, Object>>> reporteAportes() {
        Map<String, Object> reporte = new HashMap<>();
        List<Aporte> todos = aporteRepository.findAll();
        long pagados = todos.stream().filter(a -> a.getEstado() == Aporte.EstadoAporte.PAGADO).count();
        long pendientes = todos.stream().filter(a -> a.getEstado() == Aporte.EstadoAporte.PENDIENTE).count();
        reporte.put("totalAportes", todos.size());
        reporte.put("pagados", pagados);
        reporte.put("pendientes", pendientes);
        return ResponseEntity.ok(ApiResponse.ok("Reporte de aportes", reporte));
    }

    // GET /api/reportes/mantenimiento — Reporte de mantenimientos
    @GetMapping("/mantenimiento")
    public ResponseEntity<ApiResponse<List<Mantenimiento>>> reporteMantenimiento() {
        return ResponseEntity.ok(ApiResponse.ok(mantenimientoRepository.findAll()));
    }
}
