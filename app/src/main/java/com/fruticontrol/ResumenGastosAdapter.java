package com.fruticontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ResumenGastosAdapter extends ArrayAdapter<ResumenGastoDataModel> implements View.OnClickListener {

    private ArrayList<ResumenGastoDataModel> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtConceptoYFecha;
        TextView txtActividadSub;
        TextView txtTipo;
        TextView txtValor;
    }

    public ResumenGastosAdapter(ArrayList<ResumenGastoDataModel> data, Context context) {
        super(context, R.layout.resumen_gasto, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        ResumenGastoDataModel dataModel = (ResumenGastoDataModel) object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ResumenGastoDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.resumen_gasto, parent, false);
            viewHolder.txtConceptoYFecha = (TextView) convertView.findViewById(R.id.textViewConceptoYFechaGasto);
            viewHolder.txtActividadSub = (TextView) convertView.findViewById(R.id.textViewActividadYSubGasto);
            viewHolder.txtTipo = (TextView) convertView.findViewById(R.id.textViewTipoGasto);
            viewHolder.txtValor = (TextView) convertView.findViewById(R.id.textViewValorGasto);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ResumenGastosAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtConceptoYFecha.setText(dataModel.getConcepto());
        viewHolder.txtActividadSub.setText(dataModel.getActividad());
        viewHolder.txtTipo.setText(dataModel.getTipo());
        viewHolder.txtValor.setText(dataModel.getValor());

        return convertView;
    }
}
