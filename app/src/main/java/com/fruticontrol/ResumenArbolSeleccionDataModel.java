package com.fruticontrol;

public class ResumenArbolSeleccionDataModel {
    private String idArbol;
    private String tipoArbol;
    private String etapaArbol;

    ResumenArbolSeleccionDataModel(String idArbol, String tipoArbol, String etapaArbol) {
        this.idArbol = idArbol;
        this.tipoArbol = tipoArbol;
        this.etapaArbol = etapaArbol;
    }

    String getIdArbol() {
        return idArbol;
    }

    public void setIdArbol(String idArbol) {
        this.idArbol = idArbol;
    }

    String getTipoArbol() {
        return tipoArbol;
    }

    public void setTipoArbol(String tipoArbol) {
        this.tipoArbol = tipoArbol;
    }

    String getEtapaArbol() {
        return etapaArbol;
    }

    public void setEtapaArbol(String etapaArbol) {
        this.etapaArbol = etapaArbol;
    }
}
