package com.aquacontrol.repository;

import com.aquacontrol.entity.Tanque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TanqueRepository extends JpaRepository<Tanque, Long> {
    // Obtener el registro de tanque más reciente
    @Query("SELECT t FROM Tanque t ORDER BY t.fecha DESC LIMIT 1")
    Optional<Tanque> findUltimoRegistro();
}