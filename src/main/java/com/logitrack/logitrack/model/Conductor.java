package com.logitrack.logitrack.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conductores")
public class Conductor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String licencia;

    @Column(nullable = false)
    private String telefono;

    @OneToMany(mappedBy = "conductor",  cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    private List<AsignacionEnvio> asignaciones = new ArrayList<>();

    public Conductor(){    }

    public Conductor(String nombre, String licencia, String telefono) {
        this.nombre = nombre;
        this.licencia = licencia;
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<AsignacionEnvio> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<AsignacionEnvio> asignaciones) {
        this.asignaciones = asignaciones;
    }

}
