package com.fruticontrol;

import android.app.Application;

public class Token extends Application {

    private String token;
    private String granjaActual;
    private Boolean arbolEscogido;

    public Token() {
    }

    public Token(String token, String granjaActual, Boolean arbolEscogido) {
        this.token = token;
        this.granjaActual = granjaActual;
        this.arbolEscogido = arbolEscogido;
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
}
