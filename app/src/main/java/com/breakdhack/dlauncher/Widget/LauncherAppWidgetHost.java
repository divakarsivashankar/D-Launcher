package com.breakdhack.dlauncher.Widget;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;

/**
 * Created by Divakar on 2/8/16.
 */


public class LauncherAppWidgetHost extends AppWidgetHost {
    //Launcher mLauncher;
    public LauncherAppWidgetHost(Context context, int hostId) {
        super(context, hostId);
        //mLauncher = launcher;
    }
    @Override
    protected AppWidgetHostView onCreateView(Context context, int appWidgetId, AppWidgetProviderInfo appWidget) {
        return new LauncherAppWidgetHostView(context);
    }
    @Override
    public void stopListening() {
        super.stopListening();
        clearViews();
    }
//    protected void onProvidersChanged() {
//        // Once we get the message that widget packages are updated, we need to rebind items
//        // in AppsCustomize accordingly.
//        mLauncher.bindPackagesUpdated(LauncherModel.getSortedWidgetsAndShortcuts(mLauncher));
//    }
}