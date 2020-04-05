package com.rafi.ac_twiterclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SendTweetActivity extends AppCompatActivity {

    private EditText edtSendTweet;
    private Button btnSendTweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);
        edtSendTweet= findViewById(R.id.edtSendTweet);
        btnSendTweet= findViewById(R.id.btnSendTweet);
        btnSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject parseObject=new ParseObject("MyTweet");
                parseObject.put("tweet",edtSendTweet.getText().toString());
                parseObject.put("user", ParseUser.getCurrentUser().getUsername());
                final ProgressDialog pd= new ProgressDialog(SendTweetActivity.this);
                pd.setMessage("Saving in progress");
                pd.show();
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            FancyToast.makeText(SendTweetActivity.this, "Tweet Saved", Toast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                        }
                        else{
                            FancyToast.makeText(SendTweetActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                        }
                        pd.dismiss();
                    }
                });
            }
        });
    }
}
