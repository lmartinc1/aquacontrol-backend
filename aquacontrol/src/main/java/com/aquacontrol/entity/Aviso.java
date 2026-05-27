package com.aquacontrol.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Entity
@Table(name = "aviso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aviso")
    private Long idAviso;

    @NotBlank(message = "El título es obligatorio")
    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "sector", length = 50)
    private String sector; // null = aviso general para todos

    @NotNull(message = "La vigencia es obligatoria")
    @Column(name = "vigencia", nullable = false)
    private LocalDate vigencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hogar")
    @JsonProperty(access = WRITE_ONLY) //Resolviendo problema de interfaz
    private Hogar hogar; // opcional: aviso dirigido a un hogar específico
}
