package com.aquacontrol.service;

import com.aquacontrol.entity.Aporte;
import com.aquacontrol.entity.Hogar;
import com.aquacontrol.exception.BusinessException;
import com.aquacontrol.exception.ResourceNotFoundException;
import com.aquacontrol.repository.AporteRepository;
import com.aquacontrol.repository.HogarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AporteService {

    @Autowired
    private AporteRepository aporteRepository;

    @Autowired
    private HogarRepository hogarRepository;

    @Autowired
    private HogarService hogarService;

    @Transactional
    public Aporte registrar(Aporte aporte) {
        // Verificar que el hogar existe y está activo/moroso (no suspendido/inactivo)
        Hogar hogar = hogarRepository.findById(aporte.getHogar().getIdHogar())
                .orElseThrow(() -> new ResourceNotFoundException("Hogar no encontrado"));

        if (hogar.getEstado() == Hogar.EstadoHogar.INACTIVO) {
            throw new BusinessException("No se pueden registrar aportes para un hogar inactivo");
        }

        // Regla: sin pagos parciales (el monto debe coincidir con cuota fija)
        // La validación de monto exacto se puede configurar externamente si se agrega una tabla de tarifas.
        // Por ahora, se acepta el monto enviado y se valida que sea positivo (anotación en entity).

        // Verificar duplicado del mismo mes/año
        if (aporte.getMesCorrespondiente() != null && aporte.getAnioCorrespondiente() != null) {
            boolean existe = aporteRepository.existsByHogar_IdHogarAndMesCorrespondienteAndAnioCorrespondiente(
                    hogar.getIdHogar(), aporte.getMesCorrespondiente(), aporte.getAnioCorrespondiente());
            if (existe) {
                throw new BusinessException("Ya existe un aporte registrado para ese mes y año en este hogar");
            }
        }

        if (aporte.getFecha() == null) {
            aporte.setFecha(LocalDate.now());
        }

        Aporte guardado = aporteRepository.save(aporte);

        // Evaluar morosidad automáticamente después de cada aporte
        hogarService.evaluarMorosidad(hogar.getIdHogar());

        return guardado;
    }

    public Page<Aporte> listarPorHogar(Long idHogar, Pageable pageable) {
        return aporteRepository.findByHogar_IdHogar(idHogar, pageable);
    }

    public List<Aporte> listarPorHogarYFecha(Long idHogar, LocalDate inicio, LocalDate fin) {
        return aporteRepository.findByHogar_IdHogarAndFechaBetween(idHogar, inicio, fin);
    }

    public Aporte obtenerPorId(Long id) {
        return aporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aporte no encontrado con ID: " + id));
    }
}
