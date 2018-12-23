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
import com.example.jag.projetenseirb.dao.SessionDao;
import com.example.jag.projetenseirb.domain.Session;

import java.util.List;

public class ListSessionsActivity extends AppCompatActivity {

    private SessionDao sessionDao;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sessions);
        userId = getIntent().getExtras().getInt("userId");
        sessionDao = (AppDatabase.getInstance(getApplication())).sessionDao();
        sessionDao.getAllSessions(userId).observe(this, new Observer<List<Session>>() {
            @Override
            public void onChanged(@Nullable List<Session> sessions) {
                // TODO: use cards or buttons to display instead of text output
                StringBuilder sb = new StringBuilder();
                for(Session s : sessions){
                    sb.append("id: " + s.id + "\n");
                    sb.append("start: " + s.startDate + "\n");
                    sb.append("end: " + s.endDate + "\n");
                    sb.append("average speed (m/s): " + s.averageSpeed + "\n");
                    sb.append("max speed (m/s): " + s.maxSpeed + "\n");
                    sb.append("duration: " + s.duration + "\n");
                    sb.append("total distance: " + s.totalDistance + "\n");
                    sb.append("=================================\n");
                }
                ((TextView) findViewById(R.id.sessions_output)).setText(sb.toString());
            }
        });
    }

    public void selectSession(View view ) {
        int sessionId = Integer.parseInt(((EditText) findViewById(R.id.session_id)).getText().toString());
        Intent intent = new Intent(this,MapDisplayActivity.class);
        intent.putExtra("sessionId",sessionId);
        startActivity(intent);
    }

    public void startSession(View view ) {
        Intent intent = new Intent(this,SessionActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }

}
