package com.fruticontrol;

public class ResumenGranjaDataModel {

    String nombreGranja;
    String tareasGranja;
    String pendientesGranja;


    public ResumenGranjaDataModel(String nombreGranja, String tareasGranja, String pendientesGranja) {
        this.nombreGranja = nombreGranja;
        this.tareasGranja = tareasGranja;
        this.pendientesGranja = pendientesGranja;
    }


    public String getNombreGranja() {
        return nombreGranja;
    }

    public void setNombreGranja(String nombreGranja) {
        this.nombreGranja = nombreGranja;
    }

    public String getTareasGranja() {
        return tareasGranja;
    }

    public void setTareasGranja(String tareasGranja) {
        this.tareasGranja = tareasGranja;
    }

    public String getPendientesGranja() {
        return pendientesGranja;
    }

    public void setPendientesGranja(String pendientesGranja) {
        this.pendientesGranja = pendientesGranja;
    }
}
