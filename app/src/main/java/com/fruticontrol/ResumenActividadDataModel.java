package com.fruticontrol;

public class ResumenActividadDataModel {
    private String tiposActividad;
    private String fechas;
    private String progreso;

    ResumenActividadDataModel(String tiposActividad, String fechas, String progreso) {
        this.tiposActividad = tiposActividad;
        this.fechas = fechas;
        this.progreso = progreso;
    }

    String getTiposActividad() {
        return tiposActividad;
    }

    public void setTiposActividad(String tiposActividad) {
        this.tiposActividad = tiposActividad;
    }

    String getFechas() {
        return fechas;
    }

    public void setFechas(String fechas) {
        this.fechas = fechas;
    }

    String getProgreso() {
        return progreso;
    }

    public void setProgreso(String progreso) {
        this.progreso = progreso;
    }
}
