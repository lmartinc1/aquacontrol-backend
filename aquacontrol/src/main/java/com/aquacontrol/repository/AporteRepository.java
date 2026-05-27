package com.aquacontrol.repository;

import com.aquacontrol.entity.Aporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AporteRepository extends JpaRepository<Aporte, Long> {

    Page<Aporte> findByHogar_IdHogar(Long idHogar, Pageable pageable);

    List<Aporte> findByHogar_IdHogar(Long idHogar);

    List<Aporte> findByHogar_IdHogarAndFechaBetween(Long idHogar, LocalDate inicio, LocalDate fin);

    // Contar cuotas pendientes/sin pagar para detectar morosidad
    @Query("SELECT COUNT(a) FROM Aporte a WHERE a.hogar.idHogar = :idHogar AND a.estado = 'PENDIENTE'")
    long contarAportesPendientesPorHogar(@Param("idHogar") Long idHogar);

    // Verificar si ya existe aporte para ese mes/año
    boolean existsByHogar_IdHogarAndMesCorrespondienteAndAnioCorrespondiente(
            Long idHogar, String mes, Integer anio);

    boolean existsByHogar_IdHogar(Long idHogar);
}
