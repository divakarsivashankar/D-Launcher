package com.breakdhack.dlauncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Divakar
 */
public class MyPackage implements Serializable{


    private static final long serialVersionUID = 603576813202641496L;
    transient Drawable icon;
    String name, label, packageName, iconFileLocation;
    int x, y;
    boolean landscape;

    public void cacheIcons(){
        if(iconFileLocation == null){
            new File(MainActivity.act.getApplicationInfo().dataDir+"/cachedAppIcon").mkdirs();
        }
        if(icon != null){
            iconFileLocation = MainActivity.act.getApplicationInfo().dataDir+"/cachedAppIcon" +packageName + name;
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(iconFileLocation);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            if(fileOutputStream != null){
                drawableToBitmap(icon).compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } else {
            iconFileLocation = null;
        }
    }

    public Bitmap getCachedIcons(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;

        if (iconFileLocation !=null){
            File cachedIcon = new File(iconFileLocation);
            if (cachedIcon.exists()){
                return BitmapFactory.decodeFile(cachedIcon.getAbsolutePath(), options);
            }
        }

        return null;
    }

    public void addToHome(Context mContext, RelativeLayout homeViewForAdapter){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;

        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) li.inflate(R.layout.app_drawer_item, null);

        if (icon == null){
            icon = new BitmapDrawable(mContext.getResources(),getCachedIcons());
        }

        ((ImageView)ll.findViewById(R.id.icon_image)).setImageDrawable(icon);

        ((TextView)ll.findViewById(R.id.icon_text)).setText(label);

        ll.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                v.setOnTouchListener(new AppIconTouchListener());
                return false;
            }
        });

        ll.setTag(this);
        ll.setOnClickListener(new AppIconClickListener(mContext));
        homeViewForAdapter.addView(ll, 0, lp);
    }

    public void deleteIcons(){
        if (iconFileLocation!=null)
            new File(iconFileLocation).delete();
    }

    public static Bitmap drawableToBitmap (Drawable drawable){
        if (drawable instanceof BitmapDrawable)
            return ((BitmapDrawable)drawable).getBitmap();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
