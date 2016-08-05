package com.breakdhack.dlauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Created by Divakar on 2/8/16.
 */
public class ShortcutClickListener implements View.OnClickListener{

    Context mContext;

    ShortcutClickListener(Context context){

        this.mContext = context;
    }

    @Override
    public void onClick(View view) {

        Intent aData;
        aData = (Intent) view.getTag();

        mContext.startActivity(aData);
    }
}
