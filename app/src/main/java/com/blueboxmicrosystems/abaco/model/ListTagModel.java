package com.blueboxmicrosystems.abaco.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueboxmicrosystems.abaco.R;

import java.util.ArrayList;

/**
 * Created by MARAUJO on 11/19/2016.
 */

public class ListTagModel extends ArrayAdapter<String> {
    private final Activity context;
    private ArrayList<String> title = new ArrayList<String>();
    private ArrayList<String> description = new ArrayList<String>();
    private ArrayList<Integer> imageId = new ArrayList<Integer>();

    public ListTagModel(Activity context,
                        ArrayList<Integer> imageId,
                        ArrayList<String> title,
                        ArrayList<String> description) {
        super(context, R.layout.list_tag_model, title);
        this.context = context;
        this.imageId = imageId;
        this.title = title;
        this.description = description;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_tag_model, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.description);

        imageView.setImageResource(imageId.get(position));
        txtTitle.setText(title.get(position));
        txtDescription.setText(description.get(position));

        return rowView;
    }
}
