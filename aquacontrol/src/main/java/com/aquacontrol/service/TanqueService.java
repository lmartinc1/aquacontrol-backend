package com.aquacontrol.service;

import com.aquacontrol.entity.Tanque;
import com.aquacontrol.exception.ResourceNotFoundException;
import com.aquacontrol.repository.TanqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TanqueService {

    @Autowired
    private TanqueRepository tanqueRepository;

    public List<Tanque> listarTodos() {
        return tanqueRepository.findAll();
    }

    public Tanque obtenerPorId(Long id) {
        return tanqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tanque no encontrado: " + id));
    }

    public Tanque obtenerUltimo() {
        return tanqueRepository.findUltimoRegistro()
                .orElseThrow(() -> new ResourceNotFoundException("No hay registros de tanque"));
    }

    @Transactional
    public Tanque registrarNivel(Tanque tanque) {
        if (tanque.getFecha() == null) tanque.setFecha(LocalDate.now());
        Tanque guardado = tanqueRepository.save(tanque);

        // Alerta de nivel crítico (≤10%)
        if (guardado.isNivelCritico()) {
            // Aquí se puede disparar un evento o notificación futura
            System.out.println("[ALERTA] Nivel crítico del tanque: " + guardado.getPorcentajeNivel() + "%");
        }
        return guardado;
    }
}
