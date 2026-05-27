package com.aquacontrol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "distribucion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Distribucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_distribucion")
    private Long idDistribucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tanque", nullable = false)
    private Tanque tanque;

    @NotBlank(message = "El sector es obligatorio")
    @Column(name = "sector", nullable = false, length = 50)
    private String sector;

    @NotNull(message = "El día es obligatorio")
    @Column(name = "dia", nullable = false)
    private LocalDate dia;

    @NotNull(message = "La hora es obligatoria")
    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoDistribucion estado = EstadoDistribucion.PROGRAMADO;

    public enum EstadoDistribucion {
        PROGRAMADO, COMPLETADO, CANCELADO
    }
}
