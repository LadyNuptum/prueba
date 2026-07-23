package com.logitrack.logitrack.service;

import com.logitrack.logitrack.model.Envio;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Service

public class EnvioServiceMemoryImpl implements EnvioService{

    //Simular la base de datos con una lista en MEMORIA
    private final List<Envio> envios = new ArrayList<>();

    public EnvioServiceMemoryImpl(){
    envios.add(new Envio(UUID.randomUUID().toString(), "Cliente 1", "Medellin", 5.2, "PENDIENTE"));
    envios.add(new Envio(UUID.randomUUID().toString(), "Cliente 2", "Bucaramanga", 7.0, "EN_RUTA"));
    envios.add(new Envio(UUID.randomUUID().toString(), "Cliente 3", "Bogota", 11.2, "ENTREGADO"));
    }

    @Override
    public List<Envio> findAll() {
        return new ArrayList<>(envios);
    }

    @Override
    public Envio findById(String id) {
        Optional<Envio> envio = envios.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
        return envio.orElse(null);
    }

    @Override
    public Envio save(Envio envio) {
        if(envio.getId() == null || envio.getId().isEmpty()){
            envio.setId(UUID.randomUUID().toString());
        }
        envios.add(envio);
        return envio;
    }

    @Override
    public Envio update(String id, Envio envioActualizado) {
        for(int i = 0; i< envios.size(); i++){
            if (envios.get(i).getId().equals(id)){
                envioActualizado.setId(id);
                envios.set(i, envioActualizado );
                return envioActualizado;
            }
        }
        return null;
    }

    @Override
    public void deleteById(String id) {
        envios.removeIf(e -> e.getId().equals(id));
    }

    @Override
    public List<Envio> findByEstado(String estado) {
        return envios.stream()
                .filter(e -> e.getEstado().equalsIgnoreCase(estado))
                .toList();
    }
}
