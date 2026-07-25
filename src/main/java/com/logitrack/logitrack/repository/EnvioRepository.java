package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio,String> { //UUID

    //Query Methods
    //Metodos derivados del nombre
    List<Envio> findByEstado(String estado);
    //SELECT * FROM envios WHERE estado = estado;

    List<Envio> findByCliente(String cliente);

    List<Envio> findByPesoKgGreaterThan(Double peso); //GreaterThan = >

    List<Envio> findByClienteAndEstado(String cliente, String estado);

    //SELECT * FROM envios WHERE cliente = cliente AND estado = estado

    //Anatomia de query methods
    //Prefijode la accion + PropiedadDeLaEntidad + palabra clave (opcional)
}
