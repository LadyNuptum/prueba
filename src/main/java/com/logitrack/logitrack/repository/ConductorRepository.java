package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, String> {
    //Metodos derivados
    List<Conductor> findByLicencia(String licencia);
    List<Conductor> findByNombreContaining(String nombre);
}
