package com.aquacontrol.service;

import com.aquacontrol.entity.Problema;
import com.aquacontrol.exception.ResourceNotFoundException;
import com.aquacontrol.repository.ProblemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProblemaService {

    @Autowired
    private ProblemaRepository problemaRepository;

    public Page<Problema> listarTodos(Pageable pageable) {
        return problemaRepository.findAll(pageable);
    }

    public List<Problema> listarPorHogar(Long idHogar) {
        return problemaRepository.findByHogar_IdHogar(idHogar);
    }

    public List<Problema> listarPorEstado(Problema.EstadoProblema estado) {
        return problemaRepository.findByEstado(estado);
    }

    public Problema obtenerPorId(Long id) {
        return problemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problema no encontrado con ID: " + id));
    }

    @Transactional
    public Problema registrar(Problema problema) {
        problema.setFecha(LocalDate.now());
        problema.setEstado(Problema.EstadoProblema.PENDIENTE);
        return problemaRepository.save(problema);
    }

    @Transactional
    public Problema actualizarEstado(Long id, Problema.EstadoProblema nuevoEstado, String observacion) {
        Problema problema = obtenerPorId(id);
        problema.setEstado(nuevoEstado);
        if (observacion != null && !observacion.isBlank()) {
            problema.setDescripcion(problema.getDescripcion() + "\n[Actualización]: " + observacion);
        }
        return problemaRepository.save(problema);
    }
}
