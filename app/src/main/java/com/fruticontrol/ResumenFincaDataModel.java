package com.fruticontrol;

public class ResumenFincaDataModel {

    private String tareasFinca;

    ResumenFincaDataModel(String tareasFinca) {
        this.tareasFinca = tareasFinca;
    }

    String getTareasFinca() {
        return tareasFinca;
    }

    public void setTareasFinca(String tareasFinca) {
        this.tareasFinca = tareasFinca;
    }

}
