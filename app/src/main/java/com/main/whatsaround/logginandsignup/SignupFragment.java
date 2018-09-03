package com.main.whatsaround.logginandsignup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




/* @description
*   signup formulary
*
* @params
* TextView mTextViewSignUp: goes together with SpannableString to create an Underscored Text "Create New Account"
* EditText mNameEditText: used to input users name, maximum 20 characters
* EditText mPasswordEditText & mPasswordRepeatEditText. Used to create password for new user (also to make sure they match before
*   creating and sending to firebase). Both are password-type and uses a text-type hint when the field is empty
*
* Button mSignupButton sends the data to create a user in firebase database
*
* Button mFacebookButton, mInstagramButton and mGoogleButton register a user based on their social networks profiles
*
*@methods
*
* */
public final class SignupFragment extends Fragment {

    private static final String TAG = "SignupFragment";
    private static final int RC_SIGN_IN=666;
    private TextView mTextViewSignUp;
    private EditText mNameEditText;
    private EditText mPasswordEditText;
    private EditText mPasswordRepeatEditText;
    private Button mSignupButton;
    private InputMethodManager mInputMethodManager;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mEmail;
    private String mPassword;
    private FragmentManager fm;
//    private GoogleSignInOptions mGoogleSignInOptions;
//    private GoogleSignInClient mGoogleSignInClient;
//    private SignInButton mGoogleSignInButton;
//    private GoogleSignInAccount mGoogleSignInAccount;

    public static Fragment getFragment()
    {
            return new SignupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mAuth=AbstractStartingClass.getFireBaseAuth();
        fm=getFragmentManager();
        mCurrentUser=AbstractStartingClass.getFireBaseUser();
//        mGoogleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        mGoogleSignInClient= GoogleSignIn.getClient(getContext(),mGoogleSignInOptions);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {

        final View view=inflater.inflate(R.layout.signup_layout,container,false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(view);
                return false;
            }
        });
        //mTextViewSignUp and its underscore
        mTextViewSignUp=view.findViewById(R.id.text_view_register);
        SpannableString spannableString=new SpannableString(mTextViewSignUp.getText());
        spannableString.setSpan(new UnderlineSpan(),0,spannableString.length(),0);
        mTextViewSignUp.setText(spannableString);
        /////////////////////////////////////////////////////////////////////////////////////

        //mNameEditText to Enter User Name.
        mNameEditText=view.findViewById(R.id.name_edit_text);
        //Makes default text dissapear when click
        mNameEditText.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction()==MotionEvent.ACTION_UP)
                {
                    if (mNameEditText.getText().equals(R.string.enter_new_user_name_text))
                    {
                        mNameEditText.setSelection(0);//returns the cursor to the index 0
                        mNameEditText.setHint("");

                    }
                }
                return false;
            }
        });
        mNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    hideKeyboard(view);
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////
        //mPasswordEditText & mPasswordRepeatEditText configuration needs to match
        mPasswordEditText=view.findViewById(R.id.password_edit_text);
        mPasswordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v,MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_UP)
                {
                    if (mPasswordEditText.getText().equals(R.string.enter_new_password_text)) {
                        mPasswordEditText.setSelection(0);//Returns to the index 0
                        mPasswordEditText.setHint("");//sets Password to empty
                        //transforms normal text typed into password type ***
                        mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                }
                return false;
            }
        });
        mPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    hideKeyboard(view);
            }
        });

        mPasswordRepeatEditText=view.findViewById(R.id.password_repeat_edit_text);
        mPasswordRepeatEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v,MotionEvent event)
            {
                if (event.getAction()==MotionEvent.ACTION_UP)
                {
                    if (mPasswordRepeatEditText.getText().equals(R.string.enter_new_password_text)) {
                        mPasswordRepeatEditText.setSelection(0);//Returns to the index 0
                        mPasswordRepeatEditText.setHint("");//sets Password to empty
                        //transforms normal text typed into password type ***
                        mPasswordRepeatEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                }
                return false;
            }
        });
        mPasswordRepeatEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    hideKeyboard(view);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///Coding the mSignupButton
        mSignupButton=view.findViewById(R.id.accept_signup_button);
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            boolean nameSizeOk=false;
            boolean nameIsOk=false;
            @Override
            public void onClick(View v) {
                if (mNameEditText.getText().length()<8 || mNameEditText.getText().length()>50)
                {
                        mNameEditText.setHint(R.string.name_signup_failure);
                        mNameEditText.setText("");
                }
                else
                {
                    nameIsOk=checkName(mNameEditText.getText().toString());
                    nameSizeOk=true;//grants the name complies with the size required
                }

                //checks password length is at least 8 to 12, if not, fails
                if((mPasswordEditText.getText().length()<8 || mPasswordEditText.getText().length()>12))
                {
                        mPasswordEditText.setText("");
                        mPasswordRepeatEditText.setText("");
                        mPasswordEditText.setHint(R.string.passwd_signup_failure);
                        mPasswordRepeatEditText.setHint(R.string.passwd_signup_failure);
                }
                else {
                    //if password length, previously checked is ok, ensures both passwords are the same and that userName is ok aswell
                    if (!(nameSizeOk && nameIsOk))
                    {
                        mNameEditText.setText("");
                        mNameEditText.setHint(R.string.name_signup_failure);
                    }

                    if (mPasswordEditText.getText().toString().equals(mPasswordRepeatEditText.getText().toString()))
                    {

                        mEmail=mNameEditText.getText().toString();
                        mPassword=mPasswordEditText.getText().toString();
                        //...if everything goes fine, then connect to database and create user
                      //  Toast.makeText(getContext(), "WORKED", Toast.LENGTH_SHORT).show();
                        connectToFireBase(view);
                    }
                    else //..otherwise just show error message
                    {
                        mPasswordEditText.setText("");
                        mPasswordRepeatEditText.setText("");
                        mPasswordEditText.setHint(R.string.passwd_signup_failure_unmatched);
                        mPasswordRepeatEditText.setHint("");
                    }
                }
                hideKeyboard(view);//hides keyboard when clicking
            }

            //checks email address
            private boolean checkName(String s) {
                return !TextUtils.isEmpty(s) && Patterns.EMAIL_ADDRESS.matcher(s).matches();
            }
        });
//        mGoogleSignInButton=view.findViewById(R.id.google_signup_button);
//        mGoogleSignInButton.setSize(SignInButton.SIZE_ICON_ONLY);
//        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch(v.getId())
//                {
//                    case R.id.google_signup_button:
//                        signIn();
//                        break;
//                }
//            }
//        });

        return view;
    }
//    private void signIn()
//    {
//        Intent googleSignInIntent=mGoogleSignInClient.getSignInIntent();//creates signin activity
//        startActivityForResult(googleSignInIntent,RC_SIGN_IN);
//    }

//    @Override
//    public void onActivityResult(int requestCode,int resultCode,Intent data)
//    {
//        super.onActivityResult(requestCode,resultCode,data);
//
//        if(requestCode==RC_SIGN_IN)
//        {
//            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
//            try
//            {
//                // Google Sign In was successful, authenticate with Firebase
//                mGoogleSignInAccount=task.getResult(ApiException.class);
//                fireBaseAuthWithGoogle(mGoogleSignInAccount);
//            }
//            catch(ApiException e){
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//            }
//
//
//        }
//    }
//    private void fireBaseAuthWithGoogle(GoogleSignInAccount account)
//    {
//        Log.i(TAG,"fireBaseAuthWithGoogle: "+account.getId());
//        AuthCredential credential= GoogleAuthProvider.getCredential(account.getAccount().toString(),null);
//        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCredential:success");
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    fm.beginTransaction().replace(R.id.signup_layout, UserScreenFragment.newFragment(getContext())).commit();
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithCredential:failure", task.getException());
//                    Toast.makeText(getContext(), "Failed to create user", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//    }
    private void hideKeyboard(View v)
    {
        mInputMethodManager=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void connectToFireBase(View view)
    {
        Log.i(TAG,"Name: "+mEmail+" Password: "+mPassword);

        mAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Log.i(TAG,"SignInWithEmail:success");
                    mCurrentUser=mAuth.getCurrentUser();
                    AbstractStartingClass.setFireBaseCurrentUser(mCurrentUser);
                    Toast.makeText(getContext(),"New user created",Toast.LENGTH_SHORT).show();
                    callit();

                    //fm.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    mSignupButton.setVisibility(View.GONE);
                   // fm.beginTransaction().replace(R.id.signup_layout, UserScreenFragment.newFragment(getContext())).commit();

                }
                else
                {
                    Log.i(TAG,"SingInWithEmail:failure");
                    Toast.makeText(getContext(),"Authentication Failed",Toast.LENGTH_SHORT).show();
                    callit();
                }
            }

        });


    }
    synchronized void callit()
    {
        if (mCurrentUser==null)
        {
            Log.i(TAG,"user is null");
        }
        else{
            Log.i(TAG,"user is Not null");
        }
    }



}
