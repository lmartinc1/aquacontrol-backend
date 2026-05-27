package com.aquacontrol.service;

import com.aquacontrol.entity.Distribucion;
import com.aquacontrol.exception.ResourceNotFoundException;
import com.aquacontrol.repository.DistribucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DistribucionService {

    @Autowired
    private DistribucionRepository distribucionRepository;

    public List<Distribucion> listarPorSector(String sector) {
        return distribucionRepository.findBySectorOrderByDiaAscHoraAsc(sector);
    }

    public List<Distribucion> listarTodos() {
        return distribucionRepository.findAll();
    }

    public Distribucion obtenerPorId(Long id) {
        return distribucionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distribución no encontrada: " + id));
    }

    @Transactional
    public Distribucion programar(Distribucion distribucion) {
        distribucion.setEstado(Distribucion.EstadoDistribucion.PROGRAMADO);
        return distribucionRepository.save(distribucion);
    }

    @Transactional
    public Distribucion actualizarEstado(Long id, Distribucion.EstadoDistribucion estado) {
        Distribucion d = obtenerPorId(id);
        d.setEstado(estado);
        return distribucionRepository.save(d);
    }
}
