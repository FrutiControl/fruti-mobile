package com.fruticontrol;

import android.app.Application;

import java.util.ArrayList;

public class Token extends Application {

    private String token;
    private String granjaActual;
    private Boolean arbolEscogido;
    private String puntosPoligonoGranja;

    public Token() {
    }

    public Token(String token, String granjaActual, Boolean arbolEscogido, String puntosPoligonoGranja) {
        this.token = token;
        this.granjaActual = granjaActual;
        this.arbolEscogido = arbolEscogido;
        this.puntosPoligonoGranja = puntosPoligonoGranja;
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

    public String getPuntosPoligonoGranja() {
        return puntosPoligonoGranja;
    }

    public void setPuntosPoligonoGranja(String puntosPoligonoGranja) {
        this.puntosPoligonoGranja = puntosPoligonoGranja;
    }
}
