package com.blueboxmicrosystems.abaco.model;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.blueboxmicrosystems.abaco.R;
import com.blueboxmicrosystems.abaco.TagList;
import com.blueboxmicrosystems.abaco.model.entity.Tag;

import java.util.List;

/**
 * Created by MARAUJO on 12/28/2016.
 */

public class ListViewAdapter<T extends Tag> extends ArrayAdapter {

    private Activity activity;
    private List<Tag> tags;
    private Fragment callerFragment;

    public ListViewAdapter(Activity context, Fragment callerFragment, int resource, List<Tag> tags) {
        super(context, resource, tags);
        this.activity = context;
        this.tags=tags;

        this.callerFragment = callerFragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        convertView = inflater.inflate(R.layout.item_listview, null, true);

        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView description = (TextView) convertView.findViewById(R.id.lvDescription);
        final ImageView itemActions = (ImageView) convertView.findViewById(R.id.item_actions);

        //ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        //int color = generator.getColor(getItem(position));
        //int color = generator.getRandomColor();

        //get first and second letter of each String item
        String letters;

        if (tags.get(position).getName().contains(" ")) {
            letters = String.valueOf(tags.get(position).getName().charAt(0)).toUpperCase()
                    + String.valueOf(tags.get(position).getName().charAt(tags.get(position).getName().toString().indexOf(' ') + 1)).toUpperCase();
        } else {
            letters = String.valueOf(tags.get(position).getName().charAt(0)).toUpperCase()
                    + String.valueOf(tags.get(position).getName().charAt(1)).toUpperCase();
        }
        //int color = colorId.get(position);
        TextDrawable drawable = TextDrawable.builder().buildRound(letters, tags.get(position).getColor()); // radius in px

        title.setText(tags.get(position).getName());
        description.setText(tags.get(position).getDescription());
        image.setImageDrawable(drawable);

        //---
        itemActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(activity, itemActions);
                popup.getMenuInflater().inflate(R.menu.list_view_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int i = item.getItemId();
                        TagList fragment = (TagList) callerFragment;

                        if (i == R.id.action_show_details) {
                            fragment.showRecordDetail(tags.get(position));
                            return true;
                        }
                        if (i == R.id.action_edit) {
                            fragment.showCreateUpdateDialog(tags.get(position));
                            return true;
                        }
                        if (i == R.id.action_delete) {
                            fragment.deleteRecord(tags.get(position));
                            return true;
                        }
                        return onMenuItemClick(item);
                    }
                });

                popup.show();
            }
        });
        //---

        return convertView;
    }
}