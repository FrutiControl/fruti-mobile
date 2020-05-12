package com.fruticontrol;

public class ResumenActividadDataModel {

    private String tiposActividad;
    private String fechas;
    private String progreso;

    public ResumenActividadDataModel(String tiposActividad, String fechas, String progreso) {
        this.tiposActividad = tiposActividad;
        this.fechas = fechas;
        this.progreso = progreso;
    }

    public String getTiposActividad() {
        return tiposActividad;
    }

    public void setTiposActividad(String tiposActividad) {
        this.tiposActividad = tiposActividad;
    }

    public String getFechas() {
        return fechas;
    }

    public void setFechas(String fechas) {
        this.fechas = fechas;
    }

    public String getProgreso() {
        return progreso;
    }

    public void setProgreso(String progreso) {
        this.progreso = progreso;
    }
}
