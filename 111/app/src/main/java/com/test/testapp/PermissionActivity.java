package com.test.testapp;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;


/**
 *
 * Created by abu on 2017/8/14 14:47.
 */

public class PermissionActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private Listener mListener;

    @Override
    protected void onDestroy() {
        mListener = null;
        super.onDestroy();
    }

    public interface Listener{
        void onGranted();
        void onDenied();
    }

    public void checkPermission(@NonNull String[] permissions, @NonNull Listener listener){
        boolean granted = true;
        boolean rationale = false;//权限拒绝后是否解释
        for(String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED){
                granted = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,permission))
                    rationale = true;
            }
        }

        if (!granted) {
            mListener = listener;
            if(rationale)
                listener.onDenied();
            else
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
        }else {
            listener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if(mListener != null) {
                    mListener.onGranted();
                    mListener = null;
                }
            } else
            {
                if(mListener != null) {
                    mListener.onDenied();
                    mListener = null;
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
