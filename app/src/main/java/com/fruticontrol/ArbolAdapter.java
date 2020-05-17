package com.fruticontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

class Arbol{
    String id;
    String tipo;
    boolean selected;

    public Arbol(String id, String tipo) {
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

public class ArbolAdapter extends ArrayAdapter<Arbol> {

    private List<Arbol> arbolList;
    private Context context;

    public ArbolAdapter(List<Arbol> arbolList, Context context) {

        super(context, R.layout.arbol,arbolList);
        this.arbolList=arbolList;
        this.context=context;
    }

    private static class ArbolHolder{
        public TextView arbolId;
        public TextView arbolTipo;
        public CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v=convertView;

        ArbolHolder holder=new ArbolHolder();

        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.arbol,null);

            holder.arbolId=(TextView)v.findViewById(R.id.textViewIdArbolSeleccion);
            holder.arbolTipo=(TextView)v.findViewById(R.id.textViewTipoArbolSeleccion);
            holder.checkBox=(CheckBox)v.findViewById(R.id.checkBox);

            holder.checkBox.setOnCheckedChangeListener((ListaArbolesSeleccionActivity)context);
            v.setTag(holder);
        }else{
            holder=(ArbolHolder)v.getTag();
        }

        Arbol a=arbolList.get(position);
        holder.arbolId.setText(a.getId());
        holder.arbolTipo.setText(a.getTipo());
        holder.checkBox.setChecked(a.isSelected());
        holder.checkBox.setTag(a);


        return v;
    }
}
