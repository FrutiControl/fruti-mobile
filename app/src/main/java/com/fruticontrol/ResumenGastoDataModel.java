package com.fruticontrol;

public class ResumenGastoDataModel {
    private String concepto;
    private String actividad;
    private String tipo;
    private String valor;

    ResumenGastoDataModel(String concepto, String actividad, String tipo, String valor) {
        this.concepto = concepto;
        this.actividad = actividad;
        this.tipo = tipo;
        this.valor = valor;
    }

    String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
