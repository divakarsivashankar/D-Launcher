package com.breakdhack.dlauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Created by Divakar
 */
public class AppIconClickListener implements View.OnClickListener{

    Context mContext;

    AppIconClickListener(Context context){

        this.mContext = context;
    }

    @Override
    public void onClick(View view) {

        MyPackage aData;
        aData = (MyPackage) view.getTag();

        Intent appLaunchIntent = new Intent(Intent.ACTION_MAIN);
        appLaunchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cN = new ComponentName(aData.packageName, aData.name);
        appLaunchIntent.setComponent(cN);
        mContext.startActivity(appLaunchIntent);
    }
}
