package com.main.whatsaround.loggedinuser;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.main.whatsaround.logginandsignup.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyAccountActivity extends AppCompatActivity {

    private static Context sContext;
    private TextView mTitleTextView;
    private Button mAcceptButton;
    private Button mCancelButton;
    private FragmentManager mFragmentManager;
    private InputMethodManager mInputMethodManager;
    private EditText mUserNameEditText;
    private EditText mNameEditText;
    private EditText dobEditText;
    private EditText mAddressEditText;
    private EditText mZipEditText;
    private EditText mPhoneEditText;
    private EditText mOldPasswordEditText;
    private EditText mNewPasswordEditText;
    private EditText mRepeatPasswordEditText;
    private Toolbar mToolbar;

    private Pattern pattern=Pattern.compile("[a-zA-Z0-9_\\.\\+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-\\.]+");


    public Intent newIntent(Context context)
    {
        sContext=context;
        return new Intent(MyAccountActivity.this,UserScreenActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.my_account_layout);
        setTheme(R.style.MyMaterialTheme);
        mToolbar=(Toolbar)findViewById(R.id.my_account_toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.orange));
        mToolbar.setTitle(R.string.my_account_string);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //Users fields
        mTitleTextView=(TextView)findViewById(R.id.personal_data_title_text_view);
        mUserNameEditText=(EditText)findViewById(R.id.username_edit_text);
        mNameEditText=(EditText)findViewById(R.id.name_edit_text);
        dobEditText=(EditText)findViewById(R.id.dob_edit_text);
        mAddressEditText=(EditText)findViewById(R.id.address_edit_text);
        mZipEditText=(EditText)findViewById(R.id.zip_edit_text);
        mPhoneEditText=(EditText)findViewById(R.id.phone_edit_text);
        mOldPasswordEditText=(EditText)findViewById(R.id.old_password_edit_text);
        mNewPasswordEditText=(EditText)findViewById(R.id.password_edit_text);
        mRepeatPasswordEditText=(EditText)findViewById(R.id.repeat_password_edit_text);

        SpannableString spannableString=new SpannableString(mTitleTextView.getText().toString());
        spannableString.setSpan(new UnderlineSpan(),0,spannableString.length(),0);

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(findViewById(android.R.id.content));
                return false;
            }
        });
        mTitleTextView.setText(spannableString);
        mAcceptButton=(Button)findViewById(R.id.accept_button);
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void hideKeyboard(View v)
    {
        mInputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //Overrides lifecycle to call transitions
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
    //creates fadein and fadeout animation
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
