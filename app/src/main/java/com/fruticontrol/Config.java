package com.fruticontrol;

import android.app.Application;

import java.util.ArrayList;

public class Config extends Application {

    private String domain="http://10.0.2.2:8000";
    private String token;
    private String fincaActual;
    private Boolean arbolEscogido;
    private ArrayList<String> puntosPoligonoFinca;

    public Config() {
    }

    public Config(String token, String fincaActual, Boolean arbolEscogido, ArrayList<String> puntosPoligonoFinca) {
        this.token = token;
        this.fincaActual = fincaActual;
        this.arbolEscogido = arbolEscogido;
        this.puntosPoligonoFinca = puntosPoligonoFinca;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFincaActual() {
        return fincaActual;
    }

    public void setFincaActual(String fincaActual) {
        this.fincaActual = fincaActual;
    }

    public Boolean getArbolEscogido() {
        return arbolEscogido;
    }

    public void setArbolEscogido(Boolean arbolEscogido) {
        this.arbolEscogido = arbolEscogido;
    }

    public ArrayList<String> getPuntosPoligonoFinca() {
        return puntosPoligonoFinca;
    }

    public void setPuntosPoligonoFinca(ArrayList<String> puntosPoligonoFinca) {
        this.puntosPoligonoFinca = puntosPoligonoFinca;
    }
}
