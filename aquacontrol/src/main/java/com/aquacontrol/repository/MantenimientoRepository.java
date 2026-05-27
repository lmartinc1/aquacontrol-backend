package com.aquacontrol.repository;

import com.aquacontrol.entity.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {
    List<Mantenimiento> findByProblema_IdProblema(Long idProblema);
    List<Mantenimiento> findByEstado(Mantenimiento.EstadoMantenimiento estado);
    List<Mantenimiento> findByTipoActividad(Mantenimiento.TipoActividad tipo);
}
