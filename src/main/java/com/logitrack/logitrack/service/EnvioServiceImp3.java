package com.logitrack.logitrack.service;

import com.logitrack.logitrack.model.AsignacionEnvio;
import com.logitrack.logitrack.model.Conductor;
import com.logitrack.logitrack.model.Envio;
import com.logitrack.logitrack.repository.ConductorRepository;
import com.logitrack.logitrack.repository.EnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//@Service
public class EnvioServiceImp3 implements EnvioService{
    private final EnvioRepository envioRepository;
    private final ConductorRepository conductorRepository;

    @Autowired
    public EnvioServiceImp3(EnvioRepository envioRepository, ConductorRepository conductorRepository) {
        this.envioRepository = envioRepository;
        this.conductorRepository = conductorRepository;
    }

    @Override
    public List<Envio> findAll() {
        return envioRepository.findAll();
    }

    @Override
    public Envio findById(String id) {
        Optional<Envio> optionalEnvio = envioRepository.findById(id);
        return optionalEnvio.orElseThrow(() -> new RuntimeException("Envío no encontrado con ID: " + id));
    }

    @Override
    public void deleteById(String id) {
        Optional<Envio> optionalEnvio = envioRepository.findById(id);
        if (!optionalEnvio.isPresent()) {
            throw new RuntimeException("Envío no encontrado con ID: " + id);
        }
        envioRepository.deleteById(id);
    }

    @Override
    public List<Envio> findByEstado(String estado) {
        return envioRepository.findByEstado(estado);
    }

    @Override
    public Envio save(Envio envio) {
        double costo = calcularCostoEnvio(envio.getPesoKg(), envio.getDestino());
        envio.setCosto(costo);

        if (envio.getEstado() == null) {
            envio.setEstado("PENDIENTE");
        }

        if (envio.getPesoKg() > 500) {
            throw new RuntimeException("El envío no puede pesar más de 500 kg");
        }

        return (Envio) envioRepository.save(envio);
    }

    @Override
    public Envio update(String id, Envio envioActualizado) {
        Optional<Envio> optionalEnvio = envioRepository.findById(id);
        Envio existente = optionalEnvio.orElseThrow(() -> new RuntimeException("Envío no encontrado con ID: " + id));

        if ("ENTREGADO".equals(existente.getEstado())) {
            throw new RuntimeException("No se puede actualizar un envío ya entregado");
        }

        if (envioActualizado.getEstado() != null) {
            validarTransicionEstado(existente.getEstado(), envioActualizado.getEstado());
        }

        if (cambiaronPesoODestino(existente, envioActualizado)) {
            double nuevoCosto = calcularCostoEnvio(envioActualizado.getPesoKg(), envioActualizado.getDestino());
            envioActualizado.setCosto(nuevoCosto);
        }

        envioActualizado.setId(id);
        Envio envioActualizadoGuardado = (Envio) envioRepository.save(envioActualizado);
        return envioActualizadoGuardado;
    }

    public AsignacionEnvio asignarConductorAEnvio(
            String envioId,
            String conductorId,
            LocalDate fechaAsignacion,
            String responsable,
            String observaciones) {

        Optional<Envio> optionalEnvio = envioRepository.findById(envioId);
        Envio envio = optionalEnvio.orElseThrow(() -> new RuntimeException("Envío no encontrado: " + envioId));

        Optional<Conductor> optionalConductor = conductorRepository.findById(conductorId);
        Conductor conductor = optionalConductor.orElseThrow(() -> new RuntimeException("Conductor no encontrado: " + conductorId));

        envio.agregarConductor(conductor, fechaAsignacion, responsable, observaciones);


        Envio envioConAsignacion = (Envio) envioRepository.save(envio);

        Optional<AsignacionEnvio> optionalAsignacion = envioConAsignacion.getAsignaciones().stream()
                .filter(a -> a.getConductor().getId().equals(conductorId))
                .findFirst();

        return optionalAsignacion.orElseThrow(() -> new RuntimeException("Error al crear la asignación"));
    }

    private double calcularCostoEnvio(Double pesoKg, String destino) {
        double costoBase = 10000;
        if (pesoKg > 50) {
            costoBase *= 1.2;
        }
        if ("Internacional".equalsIgnoreCase(destino)) {
            costoBase *= 1.5;
        }
        return costoBase;
    }

    private void validarTransicionEstado(String actual, String nuevo) {
        boolean permitida = switch (actual) {
            case "PENDIENTE" -> nuevo.equals("EN_RUTA") || nuevo.equals("CANCELADO");
            case "EN_RUTA" -> nuevo.equals("ENTREGADO");
            case "ENTREGADO" -> false;
            default -> false;
        };
        if (!permitida) {
            throw new RuntimeException(
                    "No se puede cambiar de '" + actual + "' a '" + nuevo + "'"
            );
        }
    }

    private boolean cambiaronPesoODestino(Envio existente, Envio actualizado) {
        return !existente.getPesoKg().equals(actualizado.getPesoKg())
                || !existente.getDestino().equals(actualizado.getDestino());
    }
}
