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

public class ResumenFincasAdapter extends ArrayAdapter<ResumenFincaDataModel> implements View.OnClickListener {
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtTareasFinca;
    }

    ResumenFincasAdapter(ArrayList<ResumenFincaDataModel> data, Context context) {
        super(context, R.layout.resumen_finca, data);
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        ResumenFincaDataModel dataModel = getItem(position);
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ResumenFincaDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.resumen_finca, parent, false);
            viewHolder.txtTareasFinca = convertView.findViewById(R.id.textViewTareasFinca);
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
        viewHolder.txtTareasFinca.setText(dataModel.getTareasFinca());
        // Return the completed view to render on screen
        return convertView;
    }
}