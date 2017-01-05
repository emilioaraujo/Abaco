package com.blueboxmicrosystems.abaco.model;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.blueboxmicrosystems.abaco.MainActivity;
import com.blueboxmicrosystems.abaco.R;
import com.blueboxmicrosystems.abaco.TagList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MARAUJO on 12/28/2016.
 */

public class ColorAdapter<T extends Integer> extends ArrayAdapter implements SpinnerAdapter {
    private Activity activity;
    private final List<T> objects; // android.graphics.Color list

    public ColorAdapter(Activity context, List<T> objects) {
        super(context, R.layout.item_color, objects);
        this.activity = context;
        this.objects = objects;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //super.getDropDownView(position, convertView, parent);
        View rowView = convertView;

        LayoutInflater inflater = this.activity.getLayoutInflater();
        rowView = inflater.inflate(R.layout.item_color, null);
        //rowView.setBackgroundColor(objects.get(position));

        int color = objects.get(position);
        TextDrawable drawable = TextDrawable.builder().buildRound("", color); // radius in px
        ImageView image = (ImageView) rowView.findViewById(R.id.imageColor);
        image.setImageDrawable(drawable);

        return rowView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        LayoutInflater inflater = this.activity.getLayoutInflater();
        rowView = inflater.inflate(R.layout.item_color, null);
        //rowView.setBackgroundColor(objects.get(position));

        int color = objects.get(position);
        TextDrawable drawable = TextDrawable.builder().buildRound("", color); // radius in px
        ImageView image = (ImageView) rowView.findViewById(R.id.imageColor);
        image.setImageDrawable(drawable);

        return rowView;
    }
}