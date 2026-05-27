package com.aquacontrol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "hogar")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hogar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hogar")
    private Long idHogar;

    @NotBlank(message = "El apellido de familia es obligatorio")
    @Size(max = 100)
    @Column(name = "apellido_familia", nullable = false, length = 100)
    private String apellidoFamilia;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200)
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Size(max = 20)
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "sector", length = 50)
    private String sector;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoHogar estado = EstadoHogar.ACTIVO;

    // Relaciones
    @OneToMany(mappedBy = "hogar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Aporte> aportes;

    @OneToMany(mappedBy = "hogar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Problema> problemas;

    @OneToMany(mappedBy = "hogar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "hogar", fetch = FetchType.LAZY)
    private List<Aviso> avisos;

    public enum EstadoHogar {
        ACTIVO, MOROSO, SUSPENDIDO, INACTIVO
    }
}
