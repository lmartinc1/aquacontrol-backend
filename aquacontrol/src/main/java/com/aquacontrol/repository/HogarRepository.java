package com.aquacontrol.repository;

import com.aquacontrol.entity.Hogar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HogarRepository extends JpaRepository<Hogar, Long> {
    Page<Hogar> findAll(Pageable pageable);
    List<Hogar> findBySector(String sector);
    List<Hogar> findByEstado(Hogar.EstadoHogar estado);
    boolean existsByApellidoFamiliaAndDireccion(String apellidoFamilia, String direccion);
}
