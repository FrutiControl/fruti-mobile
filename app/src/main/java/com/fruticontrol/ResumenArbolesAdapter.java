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

public class ResumenArbolesAdapter extends ArrayAdapter<ResumenArbolDataModel> implements View.OnClickListener {
    private ArrayList<ResumenArbolDataModel> dataSet;
    private Context mContext;

    private static class ViewHolder {
        TextView txtIdArbol;
        TextView txtTipoArbol;
        TextView txtFaseArbol;
    }

    public ResumenArbolesAdapter(ArrayList<ResumenArbolDataModel> data, Context context) {
        super(context, R.layout.resumen_arbol, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        ResumenArbolDataModel dataModel = (ResumenArbolDataModel) object;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ResumenArbolDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.resumen_arbol, parent, false);
            viewHolder.txtIdArbol = convertView.findViewById(R.id.textViewConceptoYFechaGasto);
            viewHolder.txtTipoArbol = convertView.findViewById(R.id.textViewActividadYSubGasto);
            viewHolder.txtFaseArbol = convertView.findViewById(R.id.textViewTipoGasto);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ResumenArbolesAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        viewHolder.txtIdArbol.setText(dataModel.getIdArbol());
        viewHolder.txtTipoArbol.setText(dataModel.getTipoArbol());
        viewHolder.txtFaseArbol.setText(dataModel.getEtapaArbol());
        return convertView;
    }
}
