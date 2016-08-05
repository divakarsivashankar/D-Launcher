package com.breakdhack.dlauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.RelativeLayout.LayoutParams;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.TextView;

import com.breakdhack.dlauncher.Widget.LauncherAppWidgetHost;
import com.breakdhack.dlauncher.Widget.LauncherAppWidgetHostView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    MyDrawerAdapter myDrawerAdapterObject;
    AppWidgetManager appWidgetManager;
    LauncherAppWidgetHost appWidgetHost;
    int REQUEST_CREATE_APPWIDGET = 222;
    int REQUEST_CREATE_Shortcut = 333;
    MyPackage[] myPacs;
    PackageManager pm;
    GridView appDrawerGrid;
    SlidingDrawer slidingDrawer;
    RelativeLayout appHomeLayout;
    static boolean appLaunch = true;
    static Activity act;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        act = this;
        appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetHost = new LauncherAppWidgetHost(this, R.id.APPWIDGET_HOST_ID);
        appDrawerGrid = (GridView)findViewById(R.id.content);
        appHomeLayout = (RelativeLayout)findViewById(R.id.home_view);
        slidingDrawer = (SlidingDrawer)findViewById(R.id.app_drawer);
        pm = getPackageManager();
        new LoadApps().execute();
        addAppsToHome();
        //setPacs();
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                appLaunch = true;
            }
        });

        appHomeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                String[] stringItems = {getResources().getString(R.string.widget),
                        getResources().getString(R.string.shortcut)};
                builder.setItems(stringItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0 :
                                selectWidget();
                                break;
                            case 1 :
                                selectShortcut();
                                break;
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        iFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        iFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        iFilter.addDataScheme("package");
        registerReceiver(new MyPacRecevier(),iFilter);
    }

    public void selectShortcut() {
        Intent newIntent = new Intent(Intent.ACTION_PICK_ACTIVITY);
        newIntent.putExtra(Intent.EXTRA_INTENT, new Intent(Intent.ACTION_CREATE_SHORTCUT));
        startActivityForResult(newIntent, R.id.REQUEST_PICK_Shortcut);
    }

    public void selectWidget() {
        int appWidgetId = this.appWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        addEmptyData(pickIntent);
        startActivityForResult(pickIntent, R.id.REQUEST_PICK_APPWIDGET);
    }
    public void addEmptyData(Intent pickIntent) {
        ArrayList customInfo = new ArrayList();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
        ArrayList customExtras = new ArrayList();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK ) {
            if (requestCode == R.id.REQUEST_PICK_APPWIDGET) {
                configureWidget(data);
            }
            else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                createWidget(data);
            }
            else if (requestCode == R.id.REQUEST_PICK_Shortcut) {
                configureShortcut(data);
            }
            else if (requestCode == REQUEST_CREATE_Shortcut) {
                createShortcut(data);
            }
        }
        else if (resultCode == RESULT_CANCELED && data != null) {
            int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) {
                appWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
    }

    private void createShortcut(Intent intent) {
        Intent.ShortcutIconResource iconResource = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE);
        Bitmap icon                              = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON);
        String shortcutLabel                     = intent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
        Intent shortIntent                       = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);

        if (icon==null){
            if (iconResource!=null){
                Resources resources =null;
                try {
                    resources = pm.getResourcesForApplication(iconResource.packageName);
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (resources != null) {
                    int id = resources.getIdentifier(iconResource.resourceName, null, null);
                    if(resources.getDrawable(id) instanceof StateListDrawable) {
                        Drawable d = ((StateListDrawable)resources.getDrawable(id)).getCurrent();
                        icon = ((BitmapDrawable)d).getBitmap();
                    }else
                        icon = ((BitmapDrawable)resources.getDrawable(id)).getBitmap();
                }
            }
        }


        if (shortcutLabel!=null && shortIntent!=null && icon!=null){
            LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 100;
            lp.topMargin = (int) 100;

            LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout ll = (LinearLayout) li.inflate(R.layout.app_drawer_item, null);

            ((ImageView)ll.findViewById(R.id.icon_image)).setImageBitmap(icon);
            ((TextView)ll.findViewById(R.id.icon_text)).setText(shortcutLabel);

            ll.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    v.setOnTouchListener(new AppIconTouchListener());
                    return false;
                }
            });

            ll.setOnClickListener(new ShortcutClickListener(this));
            ll.setTag(shortIntent);
            appHomeLayout.addView(ll, lp);
        }

    }

    public void configureShortcut(Intent intent){
        startActivityForResult(intent, REQUEST_CREATE_Shortcut);
    }

    private void configureWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);
        if (appWidgetInfo.configure != null) {
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
        } else {
            createWidget(data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        appWidgetHost.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        appWidgetHost.stopListening();
    }

    public class LoadApps extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> pacsList = pm.queryIntentActivities(mainIntent, 0);
            myPacs = new MyPackage[pacsList.size()];
            for(int I=0;I<pacsList.size();I++){
                myPacs[I]= new MyPackage();
                myPacs[I].icon=pacsList.get(I).loadIcon(pm);
                myPacs[I].packageName=pacsList.get(I).activityInfo.packageName;
                myPacs[I].name=pacsList.get(I).activityInfo.name;
                myPacs[I].label=pacsList.get(I).loadLabel(pm).toString();
            }
            new AppNameSorting().exchange_sorting(myPacs);
            //themePacs();
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            if (myDrawerAdapterObject == null){
                myDrawerAdapterObject = new MyDrawerAdapter(act, myPacs);
                appDrawerGrid.setAdapter(myDrawerAdapterObject);
                appDrawerGrid.setOnItemClickListener(new DrawerIconClickListener(act, myPacs, pm));
                appDrawerGrid.setOnItemLongClickListener(new DrawerIconLongClickListener(act, slidingDrawer, appHomeLayout, myPacs));
            }else{
                myDrawerAdapterObject.newPacsForAdapter = myPacs;
                myDrawerAdapterObject.notifyDataSetInvalidated();
            }
        }



    }


    public void createWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);
        LauncherAppWidgetHostView hostView = (LauncherAppWidgetHostView) appWidgetHost.createView(this, appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);


        hostView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //appWidgetHost.onInterceptTouchEvent(view, );
                return false;
            }
        });

        appHomeLayout.addView(hostView);
        slidingDrawer.bringToFront();
    }

    public void addAppsToHome(){
        AppSerializableData appD  = SerializationTools.loadSerializedData();
        if (appD!=null){
            for (MyPackage pacToAddToHome : appD.apps){
                pacToAddToHome.addToHome(this, appHomeLayout);
            }

        }
    }


    public void setPacs(){
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> myPacsList = pm.queryIntentActivities(intent,0);
        myPacs = new MyPackage[myPacsList.size()];
        for(int i = 0; i < myPacsList.size(); i++){
            myPacs[i] = new MyPackage();
            myPacs[i].icon = myPacsList.get(i).loadIcon(pm);
            myPacs[i].packageName = myPacsList.get(i).activityInfo.packageName;
            myPacs[i].name = myPacsList.get(i).activityInfo.name;
            myPacs[i].label = myPacsList.get(i).loadLabel(pm).toString();
        }

        new AppNameSorting().exchange_sorting(myPacs);
        myDrawerAdapterObject = new MyDrawerAdapter(this, myPacs);
        appDrawerGrid.setAdapter(myDrawerAdapterObject);
        appDrawerGrid.setOnItemClickListener(new DrawerIconClickListener(this, myPacs, pm));
        appDrawerGrid.setOnItemLongClickListener(new DrawerIconLongClickListener(this, slidingDrawer, appHomeLayout, myPacs));

    }

    public class MyPacRecevier extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            new LoadApps().execute();
            //setPacs();
        }
    }

}
