package com.logitrack.logitrack.dto;

import com.logitrack.logitrack.model.Envio;
import org.antlr.v4.runtime.misc.NotNull;


//DTO data transfer object - Objeto de transferencia de datos
public class EnvioDTO {
    private String id;
    private String cliente;
    private String destino;
    private Double pesoKg;
    private String estado;
    private Double costo;

    public EnvioDTO() {
    }

    public EnvioDTO(String id, String cliente, String destino, Double pesoKg, String estado, Double costo) {
        this.id = id;
        this.cliente = cliente;
        this.destino = destino;
        this.pesoKg = pesoKg;
        this.estado = estado;
        this.costo = costo;
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

    public static EnvioDTO fromEntity(Envio envio){
        return new EnvioDTO(
                envio.getId(),
                envio.getCliente(),
                envio.getDestino(),
                envio.getPesoKg(),
                envio.getEstado(),
                envio.getCosto()
        );
    }
    // EnvioDTO dto = EnvioDTO.fromEntity(envioEntidad)
    public Envio toEntity(){
        return new Envio(this.cliente,this.destino,
                this.pesoKg,this.estado,this.costo);
    }
}
