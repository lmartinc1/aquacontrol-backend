package com.aquacontrol.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "aporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hogar", nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_aporte_hogar",
                    foreignKeyDefinition = "FOREIGN KEY (id_hogar) REFERENCES hogar(id_hogar) ON DELETE RESTRICT"
            ))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Resolviendo problema de interfaz
    private Hogar hogar;

    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoAporte estado = EstadoAporte.PAGADO;

    @Column(name = "mes_correspondiente", length = 20)
    private String mesCorrespondiente;

    @Column(name = "anio_correspondiente")
    private Integer anioCorrespondiente;

    public enum EstadoAporte {
        PAGADO, PENDIENTE, ANULADO
    }
}
