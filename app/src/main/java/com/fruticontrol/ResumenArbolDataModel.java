package com.fruticontrol;

public class ResumenArbolDataModel {

    String idArbol;
    String tipoArbol;
    String etapaArbol;

    public ResumenArbolDataModel(String idArbol, String tipoArbol, String etapaArbol) {
        this.idArbol = idArbol;
        this.tipoArbol = tipoArbol;
        this.etapaArbol = etapaArbol;
    }

    public String getIdArbol() {
        return idArbol;
    }

    public void setIdArbol(String idArbol) {
        this.idArbol = idArbol;
    }

    public String getTipoArbol() {
        return tipoArbol;
    }

    public void setTipoArbol(String tipoArbol) {
        this.tipoArbol = tipoArbol;
    }

    public String getEtapaArbol() {
        return etapaArbol;
    }

    public void setEtapaArbol(String etapaArbol) {
        this.etapaArbol = etapaArbol;
    }
}
