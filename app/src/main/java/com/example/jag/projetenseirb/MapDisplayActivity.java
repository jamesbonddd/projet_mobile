package com.example.jag.projetenseirb;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.jag.projetenseirb.dao.AppDatabase;
import com.example.jag.projetenseirb.dao.PositionDao;
import com.example.jag.projetenseirb.domain.Position;

import java.util.List;

public class MapDisplayActivity extends AppCompatActivity {

    private PositionDao positionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);
        positionDao = (AppDatabase.getInstance(getApplication())).positionDao();
        int sessionId = getIntent().getExtras().getInt("sessionId");
        positionDao.getAllPositions(sessionId).observe(this, new Observer<List<Position>>() {
            @Override
            public void onChanged(@Nullable List<Position> positions) {
                // TODO : USE A MAP INSTEAD OF TEXT OUTPUT
                StringBuilder sb = new StringBuilder();
                for(Position position  : positions){
                    sb.append("id: " + position.id + "\n");
                    sb.append("latitude: " + position.latitude + "\n");
                    sb.append("longitude: " + position.longitude + "\n");
                    sb.append("order: " + position.orderInSession + "\n");
                    sb.append("============================\n");
                }
                ((TextView) findViewById(R.id.positions_output) ).setText(sb.toString());
            }
        });
    }
}
