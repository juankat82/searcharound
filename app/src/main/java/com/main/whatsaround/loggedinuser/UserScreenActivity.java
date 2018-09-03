package com.main.whatsaround.loggedinuser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseUser;
import com.main.whatsaround.logginandsignup.AbstractStartingClass;
import com.main.whatsaround.logginandsignup.BoneActivity;
import com.main.whatsaround.logginandsignup.R;


public class UserScreenActivity extends AppCompatActivity{

    private Context mContext;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToogle;
    private FirebaseUser mCurrentUser;
    private final String TAG = "UserScreenFragment";
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment fragment;
    private UserScreenActivity mUserScreenActivity;
    private static TextView mUserNameTextView;
    private Intent intent;
    private Toolbar mToolbar;

    public Intent newIntent(Context context)
    {
        mContext=context;
        //change to BoneActivity.class
        Intent intent=new Intent(UserScreenActivity.this, BoneActivity.class);
        return intent;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.user_screen_layout);
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setTheme(R.style.MyMaterialTheme);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.orange));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUserScreenActivity=this;
        mNavigationView=(NavigationView)findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//                if (item.isChecked())
//                    item.setChecked(false);
//                else
//                    item.setChecked(true);

                mDrawerLayout.closeDrawers();
                mFragmentTransaction=getSupportFragmentManager().beginTransaction();
                switch(item.getItemId())
                {
                    case R.id.nav_account:
                        intent=new Intent(UserScreenActivity.this,MyAccountActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_settings:
                        intent=new Intent(UserScreenActivity.this,SettingsActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_logout:
                        intent=new Intent(UserScreenActivity.this,LogoutActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_about:
                        intent=new Intent(UserScreenActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });



        mCurrentUser=AbstractStartingClass.getFireBaseUser();
        mDrawerLayout=(DrawerLayout)findViewById(R.id.user_screen_drawer_layout);
        mActionBarDrawerToogle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open_string,R.string.close_string){
            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mActionBarDrawerToogle);
        mActionBarDrawerToogle.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUserNameTextView= findViewById(R.id.username_edit_text);
//        mUserNameTextView.setText(R.string.or_text);


//        if (mCurrentUser != null)
//            mUserNameTextView.setText(R.string.login_text);
//        else
//        {
//            mUserNameTextView.setText(R.string.or_text);
//        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToogle.onOptionsItemSelected(item)) {

            return true;//true means no more processing
        }
        return super.onOptionsItemSelected(item);
    }
    public static void writeSetView(String str)
    {
        //mUserNameTextView.setText(mUserNameTextView.getText()+str);
    }
//    @Override
//    public void onBackPressed()
//    {
//        int count=mFragmentManager.getBackStackEntryCount();//count how many fragments are in the stack
//    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        onStartNewActivity();
    }
    @Override
    public void finish() {
        super.finish();
        onLeaveThisActivity();
    }

    protected void onStartNewActivity()
    {
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
    }
    protected void onLeaveThisActivity() {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

}
