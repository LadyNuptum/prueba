package com.logitrack.logitrack.model;


import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    //EJEMPLO codigo_seguimiento VARCHAR(255) NOT NULL -> nullable = false
    @Column(nullable = false)

    private String cliente;



    @Column(nullable = false)
    private String destino;

    @Column(name = "peso_kg", nullable = false)
    private Double pesoKg;

    @Column(nullable = false)
    private String estado; // Entregado, En_ruta, Pendiente

    @Column(nullable = false)
    private Double costo;

    // Declaracion de relaciones
    @OneToMany(mappedBy = "envio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    private List<AsignacionEnvio> asignaciones = new ArrayList<>();


    public Envio() {
    }


    // Constructor Sin Id, el Id lo genera JPA automaticamente
    public Envio(String cliente, String destino, Double pesoKg, String estado, Double costo) {
        this.cliente = cliente;
        this.destino = destino;
        this.pesoKg = pesoKg;
        this.estado = estado;
        this.costo = costo;
    }

    // Constructor con ID por si quiero hacer alguna prueba con datos de ejemplo
    public Envio(String id, String cliente, String destino, Double pesoKg, String estado) {
        this.id = id;
        this.cliente = cliente;
        this.destino = destino;
        this.pesoKg = pesoKg;
        this.estado = estado;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(Double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public List<AsignacionEnvio> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<AsignacionEnvio> asignaciones) {
        this.asignaciones = asignaciones;
    }


    //Helper Method -Metodo de Conveniencia
    public void agregarConductor(Conductor conductor, LocalDate fechaAsignacion, String responsable, String observaciones){
        AsignacionEnvio asignacion = new AsignacionEnvio(this, conductor,fechaAsignacion,responsable,observaciones);
        this.asignaciones.add(asignacion);
        conductor.getAsignaciones().add(asignacion);
    }

}
