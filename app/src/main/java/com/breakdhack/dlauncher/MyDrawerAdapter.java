package com.breakdhack.dlauncher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Divakar.
 */
public class MyDrawerAdapter extends BaseAdapter {
    Context mContext;
    MyPackage[] newPacsForAdapter;
    @Override
    public int getCount() {
        return newPacsForAdapter.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    static class ViewHolder{
        TextView appText;
        ImageView appIcon;
    }

    @Override
    public View getView(int position, View newView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(newView == null){
            newView = li.inflate(R.layout.app_drawer_item, null);
            viewHolder = new ViewHolder();
            viewHolder.appText = (TextView)newView.findViewById(R.id.icon_text);
            viewHolder.appIcon = (ImageView)newView.findViewById(R.id.icon_image);
            newView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)newView.getTag();

        }
        viewHolder.appText.setText(newPacsForAdapter[position].label);
        viewHolder.appIcon.setImageDrawable(newPacsForAdapter[position].icon);
        return newView;

    }

    public MyDrawerAdapter(Context c, MyPackage myPacs[]){
        mContext = c;
        newPacsForAdapter = myPacs;
    }
}
