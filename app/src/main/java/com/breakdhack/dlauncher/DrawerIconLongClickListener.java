package com.breakdhack.dlauncher;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Divakar
 */
public class DrawerIconLongClickListener implements AdapterView.OnItemLongClickListener {
    Context mContext;
    SlidingDrawer sDrawer;
    RelativeLayout hLayout;
    MyPackage[] listenerPacs;

    public DrawerIconLongClickListener(Context c, SlidingDrawer slidingDrawer, RelativeLayout homeLayout, MyPackage[] myPacs){

        mContext = c;
        sDrawer = slidingDrawer;
        hLayout = homeLayout;
        this.listenerPacs = myPacs;

    }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        MainActivity.appLaunch = false;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(view.getWidth(), view.getHeight());
        lp.leftMargin = (int) view.getX();
        lp.topMargin = (int) view.getY();

        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout)li.inflate(R.layout.app_drawer_item, null);
        ((ImageView)ll.findViewById(R.id.icon_image)).setImageDrawable(((ImageView) view.findViewById(R.id.icon_image)).getDrawable());
        ((TextView)ll.findViewById(R.id.icon_text)).setText(((TextView) view.findViewById(R.id.icon_text)).getText());
        ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.setOnTouchListener(new AppIconTouchListener());
                return false;
            }
        });

        ll.setOnClickListener(new AppIconClickListener(mContext));

        AppSerializableData serializedAppData =SerializationTools.loadSerializedData();
        if (serializedAppData == null) {
            serializedAppData = new AppSerializableData();
        }
        if(serializedAppData.apps == null){
            serializedAppData.apps = new ArrayList<MyPackage>();
        }

        MyPackage packageToAdd = listenerPacs[i];
        packageToAdd.x = (int)view.getX();
        packageToAdd.y = (int) view.getY();
        if(MainActivity.act.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            packageToAdd.landscape = true;
        }else {
            packageToAdd.landscape = false;
        }
        packageToAdd.cacheIcons();
        ll.setTag(packageToAdd);
        serializedAppData.apps.add(packageToAdd);
        SerializationTools.serializeData(serializedAppData);
        hLayout.addView(ll, lp);
        sDrawer.animateClose();
        sDrawer.bringToFront();
        return false;
    }
}
