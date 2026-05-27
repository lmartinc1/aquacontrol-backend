package com.aquacontrol.repository;

import com.aquacontrol.entity.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvisoRepository extends JpaRepository<Aviso, Long> {

    // Avisos vigentes para un sector específico O avisos generales (sector null)
    @Query("SELECT a FROM Aviso a WHERE (a.sector = :sector OR a.sector IS NULL) AND a.vigencia >= :hoy ORDER BY a.vigencia ASC")
    List<Aviso> findAvisosVigentesPorSector(@Param("sector") String sector, @Param("hoy") LocalDate hoy);

    // Todos los avisos vigentes (para el comité)
    @Query("SELECT a FROM Aviso a WHERE a.vigencia >= :hoy ORDER BY a.vigencia ASC")
    List<Aviso> findAvisosVigentes(@Param("hoy") LocalDate hoy);
}
