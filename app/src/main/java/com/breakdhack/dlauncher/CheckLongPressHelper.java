package com.breakdhack.dlauncher;

/**
 * Created by Divakar on 2/8/16.
 */
import android.view.View;
import android.view.ViewConfiguration;


public class CheckLongPressHelper {
    private View mView;
    private boolean mHasPerformedLongPress;
    private CheckForLongPress mPendingCheckForLongPress;
    class CheckForLongPress implements Runnable {
        public void run() {
            if ((mView.getParent() != null) && mView.hasWindowFocus()
                    && !mHasPerformedLongPress) {
                if (mView.performLongClick()) {
                    mView.setPressed(false);
                    mHasPerformedLongPress = true;
                }
            }
        }
    }
    public CheckLongPressHelper(View v) {
        mView = v;
    }
    public void postCheckForLongPress() {
        mHasPerformedLongPress = false;
        if (mPendingCheckForLongPress == null) {
            mPendingCheckForLongPress = new CheckForLongPress();
        }
        mView.postDelayed(mPendingCheckForLongPress, ViewConfiguration.getLongPressTimeout());
    }
    public void cancelLongPress() {
        mHasPerformedLongPress = false;
        if (mPendingCheckForLongPress != null) {
            mView.removeCallbacks(mPendingCheckForLongPress);
            mPendingCheckForLongPress = null;
        }
    }
    public boolean hasPerformedLongPress() {
        return mHasPerformedLongPress;
    }
}


