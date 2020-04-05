package com.rafi.ac_twiterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUser extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private List<String> tUser;
    private ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_user);
        FancyToast.makeText(TwitterUser.this,"Welcome"+ParseUser.getCurrentUser().getUsername(),FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
        listView=findViewById(R.id.listView);
        tUser=new ArrayList<String>();
        arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_list_item_checked,tUser);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);
        try{
            ParseQuery<ParseUser> query= ParseUser.getQuery();
            query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(e==null && objects.size()>0){
                        for(ParseUser users: objects){
                            tUser.add(users.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                        for(String user:tUser) {
                            if (ParseUser.getCurrentUser().getList("fanOf") != null) {
                                if (ParseUser.getCurrentUser().getList("fanOf").contains(user))
                                    listView.setItemChecked(tUser.indexOf(user), true);
                            }
                        }
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout_item:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Intent intent=new Intent(TwitterUser.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });
            case R.id.sendTweetItem:
                Intent intent =new Intent(TwitterUser.this,SendTweetActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView= (CheckedTextView) view;
        if(checkedTextView.isChecked()) {
            FancyToast.makeText(TwitterUser.this,tUser.get(position) + " is following",Toast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
            ParseUser.getCurrentUser().add("fanOf",tUser.get(position));

        }
        else {
            FancyToast.makeText(TwitterUser.this,tUser.get(position) + " is not following",Toast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(tUser.get(position));
            List currentFanOf= ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentFanOf);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(TwitterUser.this," Success",Toast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                }
                else{
                    FancyToast.makeText(TwitterUser.this," Failed",Toast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                }

            }
        });
    }
}
