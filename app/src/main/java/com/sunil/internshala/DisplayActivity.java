package com.sunil.internshala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DisplayActivity extends AppCompatActivity {
    private Button dup,dwn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        dup=findViewById(R.id.dupbutton);
        dwn=findViewById(R.id.ddwbutton);
        dup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DisplayActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        dwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DisplayActivity.this,FetchData.class);
                startActivity(intent);
            }
        });
    }
}