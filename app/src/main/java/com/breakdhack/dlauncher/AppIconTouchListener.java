package com.breakdhack.dlauncher;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Divakar
 */
public class AppIconTouchListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int topMargin;
        int lMargin;

        switch (motionEvent.getAction()){

            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(view.getWidth(), view.getHeight());

                topMargin = (int) motionEvent.getRawY() - (view.getHeight()/2);
                lMargin = (int) motionEvent.getRawX() - (view.getWidth()/2);

                if(lMargin+ view.getWidth() > view.getRootView().getWidth()){
                    lMargin = view.getRootView().getWidth() - view.getWidth();
                }
                if(lMargin < 0){
                    lMargin = 0;
                }
                if(topMargin+ view.getHeight() > ((View)view.getParent()).getHeight()){
                    topMargin = ((View)view.getParent()).getHeight() - view.getHeight();
                }
                if(topMargin < 0){
                    topMargin = 0;
                }

                view.setLayoutParams(lp);
                break;

            case MotionEvent.ACTION_UP:
                view.setOnTouchListener(null);
                break;

        }

        return true;
    }
}
