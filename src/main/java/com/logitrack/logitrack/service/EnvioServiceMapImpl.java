package com.logitrack.logitrack.service;

import com.logitrack.logitrack.model.Envio;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//@Service
//@Primary

public class EnvioServiceMapImpl implements  EnvioService{

    private final Map<String, Envio> envios = new ConcurrentHashMap<>();

    public EnvioServiceMapImpl(){
        Envio e1 = new Envio(UUID.randomUUID().toString(), "Cliente 1", "Medellin", 5.2, "PENDIENTE");
        Envio e2 = new Envio(UUID.randomUUID().toString(), "Cliente 2", "Bogota", 4.1, "EN_RUTA");
        Envio e3 = new Envio(UUID.randomUUID().toString(), "Cliente 3", "Bucaramanga", 15.1, "EN_RUTA");
        Envio e4 = new Envio(UUID.randomUUID().toString(), "Cliente 4", "Cota", 2.1, "ENTREGADO");

        envios.put(e1.getId(), e1);
        envios.put(e2.getId(), e2);
        envios.put(e3.getId(), e3);
        envios.put(e4.getId(), e4);


    }

    @Override
    public List<Envio> findAll() {
        return new ArrayList<>(envios.values());
    }

    @Override
    public Envio findById(String id) {
        return envios.get(id);
    }

    @Override
    public Envio save(Envio envio) {

        if(envio.getId() == null ||envio.getId().isEmpty()){
            envio.setId(UUID.randomUUID().toString());
        }
        envios.put(envio.getId(), envio);
        return envio;
    }

    @Override
    public Envio update(String id, Envio envioActualizado) {
        if(!envios.containsKey(id)){
            return null;
        }
        envioActualizado.setId(id);
        envios.put(id, envioActualizado);
        return envioActualizado;
    }

    @Override
    public void deleteById(String id) {
        envios.remove(id);
    }

    @Override
    public List<Envio> findByEstado(String estado) {
        return envios.values().stream()
                .filter(e -> e.getEstado().equalsIgnoreCase(estado))
                .toList();
    }
}
