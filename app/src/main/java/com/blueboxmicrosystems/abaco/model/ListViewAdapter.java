package com.blueboxmicrosystems.abaco.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.blueboxmicrosystems.abaco.MainActivity;
import com.blueboxmicrosystems.abaco.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MARAUJO on 12/28/2016.
 */

public class ListViewAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private List<String> titles;
    private List<String> descriptions;
    private List<Integer> colorId;

    public ListViewAdapter(Activity context, int resource, List<String> titles, List<String> descriptions, List<Integer> colorId) {
        super(context, resource, titles);
        this.activity = context;
        this.titles = titles;
        this.descriptions=descriptions;
        this.colorId = colorId;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public String getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(titles.get(position));
        holder.description.setText(descriptions.get(position));

        //get first letter of each String item
        String letters;
        if (titles.get(position).toString().contains(" ")) {
            letters = String.valueOf(titles.get(position).charAt(0)).toUpperCase()+String.valueOf(titles.get(position).charAt(titles.get(position).toString().indexOf(' ')+1)).toUpperCase();
        }else{
            letters = String.valueOf(titles.get(position).charAt(0)).toUpperCase()+String.valueOf(titles.get(position).charAt(1)).toUpperCase();
        }

        //ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        //int color = generator.getColor(getItem(position));
        //int color = generator.getRandomColor();

        int color = colorId.get(position);
        TextDrawable drawable = TextDrawable.builder().buildRound(letters, color); // radius in px
        holder.image.setImageDrawable(drawable);

        return convertView;
    }

    private class ViewHolder {
        private ImageView image;
        private TextView title;
private TextView description;

        public ViewHolder(View v) {
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            description =(TextView) v.findViewById(R.id.description);
        }
    }
}