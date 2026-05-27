package com.aquacontrol.service;

import com.aquacontrol.entity.Hogar;
import com.aquacontrol.exception.ResourceNotFoundException;
import com.aquacontrol.exception.BusinessException;
import com.aquacontrol.repository.AporteRepository;
import com.aquacontrol.repository.HogarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HogarService {

    @Autowired
    private HogarRepository hogarRepository;

    @Autowired
    private AporteRepository aporteRepository;

    public Page<Hogar> listarTodos(Pageable pageable) {
        return hogarRepository.findAll(pageable);
    }

    public List<Hogar> listarPorSector(String sector) {
        return hogarRepository.findBySector(sector);
    }

    public Hogar obtenerPorId(Long id) {
        return hogarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hogar no encontrado con ID: " + id));
    }

    @Transactional
    public Hogar registrar(Hogar hogar) {
        hogar.setEstado(Hogar.EstadoHogar.ACTIVO);
        return hogarRepository.save(hogar);
    }

    @Transactional
    public Hogar actualizar(Long id, Hogar datos) {
        Hogar hogar = obtenerPorId(id);
        hogar.setApellidoFamilia(datos.getApellidoFamilia());
        hogar.setDireccion(datos.getDireccion());
        hogar.setTelefono(datos.getTelefono());
        hogar.setSector(datos.getSector());
        hogar.setEstado(datos.getEstado()); // FIX Bug 1: actualizar estado desde el formulario
        return hogarRepository.save(hogar);
    }

    @Transactional
    public void eliminar(Long id) {
        Hogar hogar = obtenerPorId(id);
        if (aporteRepository.existsByHogar_IdHogar(id)) {
            throw new BusinessException(
                    "No se puede eliminar el hogar. Tiene aportes registrados. " +
                            "Cambie el estado a INACTIVO en su lugar."
            );
        }
        hogarRepository.delete(hogar);
    }

    @Transactional
    public Hogar cambiarEstado(Long id, Hogar.EstadoHogar nuevoEstado) {
        Hogar hogar = obtenerPorId(id);
        hogar.setEstado(nuevoEstado);
        return hogarRepository.save(hogar);
    }

    @Transactional
    public void evaluarMorosidad(Long idHogar) {
        Hogar hogar = obtenerPorId(idHogar);
        long pendientes = aporteRepository.contarAportesPendientesPorHogar(idHogar);
        if (pendientes >= 2 && hogar.getEstado() == Hogar.EstadoHogar.ACTIVO) {
            hogar.setEstado(Hogar.EstadoHogar.MOROSO);
            hogarRepository.save(hogar);
        } else if (pendientes == 0 && hogar.getEstado() == Hogar.EstadoHogar.MOROSO) {
            hogar.setEstado(Hogar.EstadoHogar.ACTIVO);
            hogarRepository.save(hogar);
        }
    }
}