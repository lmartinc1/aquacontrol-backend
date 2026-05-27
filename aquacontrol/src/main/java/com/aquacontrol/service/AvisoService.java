package com.aquacontrol.service;

import com.aquacontrol.entity.Aviso;
import com.aquacontrol.exception.ResourceNotFoundException;
import com.aquacontrol.repository.AvisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvisoService {

    @Autowired
    private AvisoRepository avisoRepository;

    public List<Aviso> listarVigentesPorSector(String sector) {
        return avisoRepository.findAvisosVigentesPorSector(sector, LocalDate.now());
    }

    public List<Aviso> listarTodosVigentes() {
        return avisoRepository.findAvisosVigentes(LocalDate.now());
    }

    public List<Aviso> listarTodos() {
        return avisoRepository.findAll();
    }

    @Transactional
    public Aviso registrar(Aviso aviso) {
        return avisoRepository.save(aviso);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!avisoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aviso no encontrado: " + id);
        }
        avisoRepository.deleteById(id);
    }
}
