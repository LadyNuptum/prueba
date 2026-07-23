package com.logitrack.logitrack.service;

import com.logitrack.logitrack.model.Envio;

import java.util.List;

public interface EnvioService {

    List<Envio> findAll();
    Envio findById(String id);
    Envio save(Envio envio);
    Envio update(String id, Envio envio);
    void deleteById(String id);
    List<Envio> findByEstado(String estado);

}
