package com.fruticontrol;

import android.app.Application;

import java.util.ArrayList;

public class Config extends Application {

    private String domain="http://10.0.2.2:8000";
    private String token;
    private String granjaActual;
    private Boolean arbolEscogido;
    private ArrayList<String> puntosPoligonoGranja;

    public Config() {
    }

    public Config(String token, String granjaActual, Boolean arbolEscogido, ArrayList<String> puntosPoligonoGranja) {
        this.token = token;
        this.granjaActual = granjaActual;
        this.arbolEscogido = arbolEscogido;
        this.puntosPoligonoGranja = puntosPoligonoGranja;
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

    public String getGranjaActual() {
        return granjaActual;
    }

    public void setGranjaActual(String granjaActual) {
        this.granjaActual = granjaActual;
    }

    public Boolean getArbolEscogido() {
        return arbolEscogido;
    }

    public void setArbolEscogido(Boolean arbolEscogido) {
        this.arbolEscogido = arbolEscogido;
    }

    public ArrayList<String> getPuntosPoligonoGranja() {
        return puntosPoligonoGranja;
    }

    public void setPuntosPoligonoGranja(ArrayList<String> puntosPoligonoGranja) {
        this.puntosPoligonoGranja = puntosPoligonoGranja;
    }
}
