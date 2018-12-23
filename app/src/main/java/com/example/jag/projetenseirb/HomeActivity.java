package com.example.jag.projetenseirb;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jag.projetenseirb.dao.AppDatabase;
import com.example.jag.projetenseirb.dao.UserDao;
import com.example.jag.projetenseirb.domain.User;


import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userDao = (AppDatabase.getInstance(getApplication())).userDao();
        userDao.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                // update view when users list changes
                // TODO: use cards or buttons instead of string output ?
                StringBuilder sb = new StringBuilder();
                for(User user : users){
                    sb.append("id: " + user.id + "\n");
                    sb.append("name: " + user.name + "\n");
                    sb.append("lastname: " + user.lastname + "\n");
                    sb.append("alias: " + user.alias + "\n");
                    sb.append("=========================\n");
                }
                ((TextView) findViewById(R.id.users_output)).setText(sb.toString());
            }
        });

    }

    public void addUser(View view){
        String alias = ((TextView) findViewById(R.id.alias)).getText().toString();
        // TODO: user name and lastname

        User user = new User("todo","todo",alias);
        userDao.insert(user);
    }

    public void selectUser(View view){
        // TODO: clickable cards or buttons instead ?
        int userId = Integer.parseInt(((EditText) findViewById(R.id.user_id)).getText().toString());
        Intent intent =  new Intent(this,ListSessionsActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }

}
