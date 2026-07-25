package com.logitrack.logitrack.service;

import com.logitrack.logitrack.model.AsignacionEnvio;
import com.logitrack.logitrack.model.Conductor;
import com.logitrack.logitrack.model.Envio;
import com.logitrack.logitrack.repository.ConductorRepository;
import com.logitrack.logitrack.repository.EnvioRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//@Service
public class EnvioServiceImp2 implements EnvioService{

    private final EnvioRepository envioRepository;
    private final ConductorRepository conductorRepository;
    private final EntityManager entityManager;

    @Autowired
    public EnvioServiceImp2(
            EnvioRepository envioRepository,
            ConductorRepository conductorRepository,
            EntityManager entityManager) {
        this.envioRepository = envioRepository;
        this.conductorRepository = conductorRepository;
        this.entityManager = entityManager;
    }



    @Override
    public List<Envio> findAll() {
        return envioRepository.findAll();
    }

    @Override
    public Envio findById(String id) {

        Optional<Envio> optionalEnvio = envioRepository.findById(id);


        if (optionalEnvio.isPresent()) {

            return optionalEnvio.get();
        } else {

            throw new RuntimeException("Envío no encontrado con ID: " + id);
        }
    }

    @Override
    public void deleteById(String id) {

        Optional<Envio> optionalEnvio = envioRepository.findById(id);


        if (optionalEnvio.isPresent()) {

            envioRepository.deleteById(id);
        } else {

            throw new RuntimeException("Envío no encontrado con ID: " + id);
        }
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
        entityManager.persist(envio);
        return envio;
    }

    @Override
    public Envio update(String id, Envio envioActualizado) {

        Optional<Envio> optionalEnvio = envioRepository.findById(id);
        Envio existente = null;

        if (optionalEnvio.isPresent()) {
            existente = optionalEnvio.get();
        } else {
            throw new RuntimeException("Envío no encontrado con ID: " + id);
        }

        if (existente.getEstado().equals("ENTREGADO")) {
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
        Envio envioActualizadoGuardado = entityManager.merge(envioActualizado);

        return envioActualizadoGuardado;
    }


    public AsignacionEnvio asignarConductorAEnvio(
            String envioId,
            String conductorId,
            LocalDate fechaAsignacion,
            String responsable,
            String observaciones) {

        Optional<Envio> optionalEnvio = envioRepository.findById(envioId);
        Envio envio = null;

        if (optionalEnvio.isPresent()) {
            envio = optionalEnvio.get();
        } else {
            throw new RuntimeException("Envío no encontrado: " + envioId);
        }

        Optional<Conductor> optionalConductor = conductorRepository.findById(conductorId);
        Conductor conductor = null;

        if (optionalConductor.isPresent()) {
            conductor = optionalConductor.get();
        } else {
            throw new RuntimeException("Conductor no encontrado: " + conductorId);
        }

        envio.agregarConductor(conductor, fechaAsignacion, responsable, observaciones);


        Envio envioConAsignacion = entityManager.merge(envio);

        AsignacionEnvio asignacionEncontrada = null;

        for (AsignacionEnvio asignacion : envioConAsignacion.getAsignaciones()) {
            if (asignacion.getConductor().getId().equals(conductorId)) {
                asignacionEncontrada = asignacion;
                break; // Salimos del ciclo porque ya la encontramos
            }
        }

        if (asignacionEncontrada != null) {
            return asignacionEncontrada;
        } else {
            throw new RuntimeException("Error al crear la asignación");
        }
    }


    private double calcularCostoEnvio(Double pesoKg, String destino) {
        double costoBase = 10000;

        if (pesoKg > 50) {
            costoBase = costoBase * 1.2;  // Aumento del 20%
        }

        if (destino.equalsIgnoreCase("Internacional")) {
            costoBase = costoBase * 1.5;  // Aumento del 50%
        }

        return costoBase;
    }

    private void validarTransicionEstado(String estadoActual, String nuevoEstado) {
        boolean transicionPermitida = false;

        if (estadoActual.equals("PENDIENTE")) {
            if (nuevoEstado.equals("EN_RUTA") || nuevoEstado.equals("CANCELADO")) {
                transicionPermitida = true;
            }
        } else if (estadoActual.equals("EN_RUTA")) {
            if (nuevoEstado.equals("ENTREGADO")) {
                transicionPermitida = true;
            }
        } else if (estadoActual.equals("ENTREGADO")) {
            // Si ya está entregado, no se puede cambiar a nada
            transicionPermitida = false;
        }

        if (!transicionPermitida) {
            throw new RuntimeException(
                    "No se puede cambiar de '" + estadoActual + "' a '" + nuevoEstado + "'"
            );
        }
    }


    private boolean cambiaronPesoODestino(Envio existente, Envio actualizado) {
        boolean pesoCambio = !existente.getPesoKg().equals(actualizado.getPesoKg());
        boolean destinoCambio = !existente.getDestino().equals(actualizado.getDestino());

        return pesoCambio || destinoCambio;
    }
}
