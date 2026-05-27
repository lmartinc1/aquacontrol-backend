package com.aquacontrol.repository;

import com.aquacontrol.entity.Problema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemaRepository extends JpaRepository<Problema, Long> {
    Page<Problema> findAll(Pageable pageable);
    List<Problema> findByHogar_IdHogar(Long idHogar);
    List<Problema> findByEstado(Problema.EstadoProblema estado);
    List<Problema> findByTipo(String tipo);
}
