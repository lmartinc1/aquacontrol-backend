package com.aquacontrol.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Entity
@Table(name = "mantenimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento")
    private Long idMantenimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_problema", nullable = false)
    @JsonProperty(access = WRITE_ONLY)
    private Problema problema;

    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoMantenimiento estado = EstadoMantenimiento.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_actividad", length = 30)
    private TipoActividad tipoActividad = TipoActividad.TECNICO;

    public enum EstadoMantenimiento {
        PENDIENTE, EN_PROCESO, COMPLETADO, CANCELADO
    }

    public enum TipoActividad {
        TECNICO,
        ADMINISTRATIVO
    }
}