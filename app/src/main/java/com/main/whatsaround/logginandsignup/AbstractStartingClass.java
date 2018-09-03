package com.main.whatsaround.logginandsignup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public abstract class AbstractStartingClass extends AppCompatActivity {


    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=0;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    private final int MY_PERMISSIONS_REQUEST_USE_FINGERPRINT=2;
    private final int MY_PERMISSIONS_REQUEST_INTERNET=3;
    private int[] permissionsSet=new int[]{0,0,0,0};
    private final String TAG="AbstractStartingClass";
    private static FirebaseAuth mAuth;
    private static FirebaseUser currentUser;
    private Fragment fragment;
    private FragmentManager fm;
    private static Context sContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fm=getSupportFragmentManager();
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        sContext=getApplicationContext();



        //check permissions and request them. See Manifest file
        checkRequestPermissions(Manifest.permission.ACCESS_COARSE_LOCATION);
        checkRequestPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        //this works to check the presence of a fingerprint reader
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            FingerprintManager fingerprintManager=(FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            try{
                if (fingerprintManager.isHardwareDetected())
                {
                    checkRequestPermissions(Manifest.permission.USE_FINGERPRINT);

                }
            }
            catch(NullPointerException e){}
            //this also works to check the presence of a fingerprint reader
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT))
//            Toast.makeText(this,"Twat",Toast.LENGTH_SHORT).show();
            //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
//            {
//              checkRequestPermissions(Manifest.permission.USE_FINGERPRINT);
//        }
        }
        checkRequestPermissions(Manifest.permission.INTERNET);
        setContentView(getLayoutResId());

    }



    public static AppCompatActivity getAppCompatActivity()
    {
        return new AppCompatActivity();
    }
    @LayoutRes
    private int getLayoutResId()
    {
        return R.layout.activity_main;
    }

    private void checkRequestPermissions(String permissionName)
    {
        //if permission is not granted
        if (ContextCompat.checkSelfPermission(this, permissionName)!= PackageManager.PERMISSION_GRANTED)
        {
            //check if permission has ever been requested and give a small explanation
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,permissionName))
//            {
//                if (!permissionName.equals(Manifest.permission.ACCESS_FINE_LOCATION )|| !permissionName.equals(Manifest.permission.ACCESS_COARSE_LOCATION))
//                    FancyToast.makeText(this,permissionName+" is required, not allowing it will require to manually turn it on later.", FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
//            }
//            else //No explanation needed, request the permission
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,permissionName))
            {

                switch (permissionName)
                {
                    case Manifest.permission.ACCESS_COARSE_LOCATION:
                        ActivityCompat.requestPermissions(this,new String[]{permissionName},MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                        break;
                    case Manifest.permission.ACCESS_FINE_LOCATION:
                        ActivityCompat.requestPermissions(this,new String[]{permissionName},MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        break;
                    case Manifest.permission.USE_FINGERPRINT:
                        ActivityCompat.requestPermissions(this,new String[]{permissionName},MY_PERMISSIONS_REQUEST_USE_FINGERPRINT);
                        break;
                    case Manifest.permission.INTERNET:
                        ActivityCompat.requestPermissions(this,new String[]{permissionName},MY_PERMISSIONS_REQUEST_INTERNET);
                        break;
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults)
    {
        switch(requestCode)
        {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION:
                //If request is cancelled, the grantResults array is empty
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    permissionsSet[0]=1;
                }
                break;
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    permissionsSet[1]=1;
                }
                break;
            case MY_PERMISSIONS_REQUEST_USE_FINGERPRINT:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    permissionsSet[2]=1;
                }
                break;
            case MY_PERMISSIONS_REQUEST_INTERNET:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    permissionsSet[3]=1;
                }
                break;
        }


    }
    public static FirebaseAuth getFireBaseAuth()
    {
        return mAuth;
    }
    public static FirebaseUser getFireBaseUser()
    {
        return currentUser;
    }

    public static void setFireBaseCurrentUser(FirebaseUser newCurrentUser)
    {
        currentUser=newCurrentUser;
    }


   // public abstract Fragment createFragment();
    public abstract Intent newIntent(Context sContext);
}

