package com.breakdhack.dlauncher.Widget;

/**
 * Created by Divakar on 2/8/16.
 */
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.breakdhack.dlauncher.CheckLongPressHelper;

public class LauncherAppWidgetHostView extends AppWidgetHostView {
    private CheckLongPressHelper mLongPressHelper;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mPreviousOrientation;
    public LauncherAppWidgetHostView(Context context) {
        super(context);
        mContext = context;
        mLongPressHelper = new CheckLongPressHelper(this);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void updateAppWidget(RemoteViews remoteViews) {
        // Store the orientation in which the widget was inflated
        mPreviousOrientation = mContext.getResources().getConfiguration().orientation;
        super.updateAppWidget(remoteViews);
    }
    public boolean orientationChangedSincedInflation() {
        int orientation = mContext.getResources().getConfiguration().orientation;
        if (mPreviousOrientation != orientation) {
            return true;
        }
        return false;
    }
    public boolean onInterceptTouchEvent( MotionEvent motionEvent) {
        // Consume any touch events for ourselves after longpress is triggered
        if (mLongPressHelper.hasPerformedLongPress()) {
            mLongPressHelper.cancelLongPress();
            return true;
        }
        // Watch for longpress events at this level to make sure
        // users can always pick up this widget
        switch (motionEvent.getAction()) {


            case MotionEvent.ACTION_DOWN: {
                mLongPressHelper.postCheckForLongPress();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLongPressHelper.cancelLongPress();
                break;
        }
        // Otherwise continue letting touch events fall through to children
        return false;
    }
    @Override
    public void cancelLongPress() {
        super.cancelLongPress();
        mLongPressHelper.cancelLongPress();
    }
    @Override
    public int getDescendantFocusability() {
        return ViewGroup.FOCUS_BLOCK_DESCENDANTS;
    }
}