package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.AsignacionEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionEnvioRepository extends JpaRepository<AsignacionEnvio,String> {

    List<AsignacionEnvio> findByEnvio_id(String envioId);
    List<AsignacionEnvio> findByConductor_id(String conductorId);

}
