package com.fruticontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

class Arbol {
    private String id;
    private String tipo;
    private boolean selected;

    Arbol(String id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    boolean isSelected() {
        return selected;
    }

    void setSelected(boolean selected) {
        this.selected = selected;
    }
}

public class ArbolAdapter extends ArrayAdapter<Arbol> {

    private List<Arbol> arbolList;
    private Context context;

    ArbolAdapter(List<Arbol> arbolList, Context context) {
        super(context, R.layout.arbol, arbolList);
        this.arbolList = arbolList;
        this.context = context;
    }

    private static class ArbolHolder {
        TextView arbolId;
        TextView arbolTipo;
        CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ArbolHolder holder = new ArbolHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater != null ? inflater.inflate(R.layout.arbol, null) : null;
            holder.arbolId = v.findViewById(R.id.textViewIdArbolSeleccion);
            holder.arbolTipo = v.findViewById(R.id.textViewTipoArbolSeleccion);
            holder.checkBox = v.findViewById(R.id.checkBox);
            holder.checkBox.setOnCheckedChangeListener((ListaArbolesSeleccionActivity) context);
            v.setTag(holder);
        } else {
            holder = (ArbolHolder) v.getTag();
        }
        Arbol a = arbolList.get(position);
        holder.arbolId.setText(a.getId());
        holder.arbolTipo.setText(a.getTipo());
        holder.checkBox.setChecked(a.isSelected());
        holder.checkBox.setTag(a);
        return v;
    }
}
