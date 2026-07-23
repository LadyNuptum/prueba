package com.logitrack.logitrack.controller;


import com.logitrack.logitrack.model.Envio;
import com.logitrack.logitrack.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //Le dice a Spring que esta clase va a manejar las peticiones HTTP (GET- PUT - POST - DELETE)
@RequestMapping("/api/envios") //http://localhost:8080/api/envios
public class EnvioController {

    //Inyeccion de dependencias por constructor

    private final EnvioService envioService;

    @Autowired
    public EnvioController(EnvioService envioService){
        this.envioService = envioService;
    }

    @GetMapping
    public List<Envio> getAllEnvios(){
        return envioService.findAll();
    }

    @GetMapping("/{id}")
    public Envio getEnvioById(@PathVariable String id){
        return envioService.findById(id);
    }

    @GetMapping("/estado/{estado}")
    public List<Envio> getEnviosByEstado(@PathVariable String estado){
        return envioService.findByEstado(estado);
    }

    @PostMapping // Crear nuevo
    public Envio createEnvio(@RequestBody Envio envio){
        return envioService.save(envio);
    }

    @PutMapping("/{id}") // Actualizar
    public Envio updateEnvio(@PathVariable String id, @RequestBody Envio envio){
        return envioService.update(id, envio);
    }
    @DeleteMapping("/{id}")
    public void deleteEnvio(@PathVariable String id){ //@PatchMapping
        envioService.deleteById(id);
    }





}
