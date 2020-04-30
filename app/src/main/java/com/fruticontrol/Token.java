package com.fruticontrol;

import android.app.Application;

public class Token extends Application {

    private String token;
    private String granjaActual;

    public Token() {
    }

    public Token(String token, String granjaActual) {
        this.token = token;
        this.granjaActual = granjaActual;
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
}
