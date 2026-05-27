package com.aquacontrol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tanque")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tanque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tanque")
    private Long idTanque;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Positive
    @Column(name = "capacidad", nullable = false, precision = 10, scale = 2)
    private BigDecimal capacidad;

    @NotNull
    @Column(name = "nivel_actual", nullable = false, precision = 10, scale = 2)
    private BigDecimal nivelActual;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @OneToMany(mappedBy = "tanque", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Distribucion> distribuciones;

    // Porcentaje calculado
    @Transient
    public Double getPorcentajeNivel() {
        if (capacidad == null || capacidad.compareTo(BigDecimal.ZERO) == 0) return 0.0;
        return nivelActual.divide(capacidad, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue();
    }

    @Transient
    public boolean isNivelCritico() {
        return getPorcentajeNivel() <= 10.0;
    }
}
