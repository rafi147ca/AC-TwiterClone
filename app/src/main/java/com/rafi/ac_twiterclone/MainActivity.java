package com.rafi.ac_twiterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtEnterEmail,edtEnterPassword,edtUsername;
    private Button btnSignUp,btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        edtEnterEmail= findViewById(R.id.edtEnterEmail);
        edtEnterPassword= findViewById(R.id.edtEnterPassword);
        edtUsername= findViewById(R.id.edtUsername);
        btnSignUp= findViewById(R.id.btnSignUp);
        btnLogIn= findViewById(R.id.btnLogIn);

        edtEnterPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnSignUp);
                }
                return false;
            }
        });
        edtEnterEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edtEnterEmail .setHint("");
                } else {
                    edtEnterEmail .setHint("Please enter an email...");
                }
            }
        });
        edtEnterPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edtEnterPassword .setHint("");
                } else {
                    edtEnterPassword .setHint("Please enter a password");
                }
            }
        });
        edtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edtUsername .setHint("");
                } else {
                    edtUsername .setHint("Please enter username");
                }
            }
        });
        btnLogIn.setOnClickListener(MainActivity.this);
        btnSignUp.setOnClickListener(MainActivity.this);

    }
    public void rootLayoutTapped(View v){
        try {
            Log.i("mytag","insideRootLayoutTapped");
            InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogIn:
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSignUp:
                if(edtEnterEmail.equals("") || edtEnterPassword.equals("") || edtUsername.equals("")){
                    FancyToast.makeText(this,"Please enter valid Email/UserID/Password",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                }
                else{
                    ParseUser parseUser= new ParseUser();
                    parseUser.setUsername(edtUsername.getText().toString());
                    parseUser.setPassword(edtEnterPassword.getText().toString());
                    parseUser.setEmail(edtEnterEmail.getText().toString());
                    ProgressDialog pd= new ProgressDialog(this);
                    pd.setMessage("Signing Up");
                    pd.show();
                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                FancyToast.makeText(MainActivity.this,"Sign up successfull",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                                socailMediaActivty();
                            }
                            else{
                                FancyToast.makeText(MainActivity.this,"There was an error :"+e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                            }


                        }
                    });
                    pd.dismiss();
                }
                break;
        }

    }
    private void socailMediaActivty(){
        Intent intent=new Intent(MainActivity.this,TwitterUser.class);
        startActivity(intent);
        finish();
    }


}
