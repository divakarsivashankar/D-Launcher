package com.breakdhack.dlauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Divakar.
 */
public class DrawerIconClickListener implements AdapterView.OnItemClickListener {
    Context mContext;
    MyPackage[] listenerPacsForAdapter;
    PackageManager pmForMyListener;

    public DrawerIconClickListener(Context c, MyPackage[] myPacs, PackageManager pm){
        mContext = c;
        listenerPacsForAdapter = myPacs;
        pmForMyListener = pm;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        if(MainActivity.appLaunch) {
            Intent appLaunchIntent = new Intent(Intent.ACTION_MAIN);
            appLaunchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cN = new ComponentName(listenerPacsForAdapter[pos].packageName, listenerPacsForAdapter[pos].name);
            appLaunchIntent.setComponent(cN);
            mContext.startActivity(appLaunchIntent);
        }

    }
}
