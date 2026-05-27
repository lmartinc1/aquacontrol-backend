package com.aquacontrol.service;

import com.aquacontrol.entity.Mantenimiento;
import com.aquacontrol.entity.Problema;
import com.aquacontrol.exception.ResourceNotFoundException;
import com.aquacontrol.repository.MantenimientoRepository;
import com.aquacontrol.repository.ProblemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MantenimientoService {

    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    @Autowired
    private ProblemaRepository problemaRepository;

    public List<Mantenimiento> listarPorProblema(Long idProblema) {
        return mantenimientoRepository.findByProblema_IdProblema(idProblema);
    }

    public Mantenimiento obtenerPorId(Long id) {
        return mantenimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento no encontrado: " + id));
    }

    @Transactional
    public Mantenimiento registrar(Mantenimiento mantenimiento) {
        // Verificar que el problema existe
        Problema problema = problemaRepository.findById(mantenimiento.getProblema().getIdProblema())
                .orElseThrow(() -> new ResourceNotFoundException("Problema no encontrado"));

        mantenimiento.setFecha(LocalDate.now());
        mantenimiento.setEstado(Mantenimiento.EstadoMantenimiento.PENDIENTE);

        Mantenimiento guardado = mantenimientoRepository.save(mantenimiento);

        // Si el mantenimiento es técnico, pasar el problema a EN_PROCESO
        if (mantenimiento.getTipoActividad() == Mantenimiento.TipoActividad.TECNICO
                && problema.getEstado() == Problema.EstadoProblema.PENDIENTE) {
            problema.setEstado(Problema.EstadoProblema.EN_PROCESO);
            problemaRepository.save(problema);
        }

        return guardado;
    }

    @Transactional
    public Mantenimiento actualizarEstado(Long id, Mantenimiento.EstadoMantenimiento estado) {
        Mantenimiento m = obtenerPorId(id);
        m.setEstado(estado);
        return mantenimientoRepository.save(m);
    }
}
