package com.sunil.internshala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class DisplayActivity extends AppCompatActivity {
    private Button dup,dwn;

    private DrawerLayout drawerLayout;
    private CoordinatorLayout coordinatorLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FrameLayout rameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        dup=findViewById(R.id.dupbutton);
        dwn=findViewById(R.id.ddwbutton);
        drawerLayout=findViewById(R.id.drawerlayout);
        coordinatorLayout=findViewById(R.id.coor);
        navigationView=findViewById(R.id.navview);
        toolbar=findViewById(R.id.toolbar);
        rameLayout=findViewById(R.id.frame);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hey !! Welcome");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle
                (DisplayActivity.this,drawerLayout,
                        R.string.open_drawer,R.string.close_drawer);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch(id){
                    case R.id.rate:
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com")));
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com" + getPackageName())));
                        }
                        break;
                    case R.id.share:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "check out this");
                        shareIntent.setPackage("com.whatsapp");
                        startActivity(Intent.createChooser(shareIntent, "Share using"));
                        break;
                }
                return false;
            }
        });

        dup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetConnected()){
                    Intent intent=new Intent(DisplayActivity.this,MainActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(DisplayActivity.this, "Connect Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetConnected()){
                    Intent intent=new Intent(DisplayActivity.this,FetchData.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(DisplayActivity.this, "Connect Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
        // making hamburger icon working
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id =item.getItemId();

        // icon is placed at home button on action bar
        if (id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);

    }

    public boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}