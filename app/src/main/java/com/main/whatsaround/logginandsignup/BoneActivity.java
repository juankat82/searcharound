package com.main.whatsaround.logginandsignup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.main.whatsaround.loggedinuser.UserScreenActivity;


    public class BoneActivity extends AbstractStartingClass {

        private FirebaseUser mCurrentUser;
        private String TAG="MainActivity";
        private static Context sContext;


        @Override
        public Intent newIntent(Context context) {
            sContext=context;
            return new Intent(sContext,MainActivity.class);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            mCurrentUser=AbstractStartingClass.getFireBaseUser();
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            //change  UserScreenActivity for MainActivity.class once the menu is implemented so it forces the login
            Intent intent=new Intent(BoneActivity.this,UserScreenActivity.class);
            startActivity(intent);
        }
    }











//        return  UserScreenFragment.newFragment(getApplicationContext());
//   UNCOMMENT THIS AFTER FIXING USERSCREENFRAGMENT LAYOUT FILE
//        if (mCurrentUser==null || account==null)
//        {
//            Log.i(TAG,"user doesnt exist");
//
//            mFragment=MainFragment.newFragment(getApplicationContext());
//            return mFragment;
//        }
//        else
//        {
//          if (mCurrentUser!=null || account!=null)
//          {
//              Log.i(TAG,"user is logged and exist");
//              mFragment=UserScreenFragment.newFragment(getApplicationContext());
//              return mFragment;
//          }
//          else {
//              Log.i(TAG,"user doesnt exist");
//              mFragment=MainFragment.newFragment(getApplicationContext());
//              return mFragment;
//          }
//
//        }



