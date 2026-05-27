package com.aquacontrol.repository;

import com.aquacontrol.entity.Distribucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistribucionRepository extends JpaRepository<Distribucion, Long> {
    List<Distribucion> findBySector(String sector);
    List<Distribucion> findBySectorOrderByDiaAscHoraAsc(String sector);
    List<Distribucion> findByEstado(Distribucion.EstadoDistribucion estado);
}
