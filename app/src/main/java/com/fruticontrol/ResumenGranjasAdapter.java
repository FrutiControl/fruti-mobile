package com.fruticontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fruticontrol.R;
import com.fruticontrol.ResumenGranjaDataModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ResumenGranjasAdapter extends ArrayAdapter<ResumenGranjaDataModel> implements View.OnClickListener{

    private ArrayList<ResumenGranjaDataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtNombreGranja;
        TextView txtTareasGranja;
        TextView txtPendientesGranja;
        ImageView logoGranja;
    }

    public ResumenGranjasAdapter(ArrayList<ResumenGranjaDataModel> data, Context context) {
        super(context, R.layout.resumen_granja, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        ResumenGranjaDataModel dataModel=(ResumenGranjaDataModel) object;

        switch (v.getId())
        {
            case R.id.imageLogoGranja:
                Snackbar.make(v, "Release date " , Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ResumenGranjaDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.resumen_granja, parent, false);
            viewHolder.txtNombreGranja= (TextView) convertView.findViewById(R.id.textViewNombreGranja);
            viewHolder.txtTareasGranja= (TextView) convertView.findViewById(R.id.textViewTareasGranja);
            viewHolder.txtPendientesGranja= (TextView) convertView.findViewById(R.id.textViewPendientesGranja);
            viewHolder.logoGranja= (ImageView) convertView.findViewById(R.id.imageLogoGranja);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtNombreGranja.setText(dataModel.getNombreGranja());
        viewHolder.txtTareasGranja.setText(dataModel.getTareasGranja());
        viewHolder.txtPendientesGranja.setText(dataModel.getPendientesGranja());
        viewHolder.logoGranja.setOnClickListener(this);
        viewHolder.logoGranja.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}