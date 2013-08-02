package com.artum.shootmaniacenter.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.artum.shootmaniacenter.R;
import com.artum.shootmaniacenter.global.Variables;

/**
 * Created by jacop_000 on 04/06/13.
 *
 * Adapter della ListView del menu.
 */
public class menuItemAdapter extends BaseAdapter{
    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater=null;

    public menuItemAdapter(Activity a, String[] d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
        {
            if(position != Variables.menu_selected)
                vi = inflater.inflate(R.layout.menu_item, null);
            else
                vi = inflater.inflate(R.layout.menu_selected_item, null);
        }
        TextView name = (TextView)vi.findViewById(R.id.menu_item_text);
        //ImageView image = (ImageView)vi.findViewById(R.id.menu_icon);
        name.setText(data[position]);
        /*switch(position)
        {
            case 0:
                image.setImageResource(R.drawable.ic_launcher);
                break;
            case 2:
                image.setImageResource(android.R.drawable.sym_contact_card);
                break;
            case 4:
                image.setImageResource(android.R.drawable.ic_menu_preferences);
                break;
            default:
                image.setVisibility(0);
                break;
        }*/

        return vi;
    }
}
