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

public class ResumenActividadesAdapter extends ArrayAdapter<ResumenActividadDataModel> implements View.OnClickListener {
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtTiposAc;
        TextView txtFechas;
        TextView txtProgreso;
    }

    public ResumenActividadesAdapter(ArrayList<ResumenActividadDataModel> data, Context context) {
        super(context, R.layout.resumen_actividad, data);
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        ResumenActividadDataModel dataModel = getItem(position);
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ResumenActividadDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.resumen_actividad, parent, false);
            viewHolder.txtTiposAc = convertView.findViewById(R.id.textViewTiposActividades);
            viewHolder.txtFechas = convertView.findViewById(R.id.textViewRangosFechas);
            viewHolder.txtProgreso = convertView.findViewById(R.id.textViewProgreso);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        assert dataModel != null;
        viewHolder.txtTiposAc.setText(dataModel.getTiposActividad());
        viewHolder.txtFechas.setText(dataModel.getFechas());
        viewHolder.txtProgreso.setText(dataModel.getProgreso());
        // Return the completed view to render on screen
        return convertView;
    }
}
