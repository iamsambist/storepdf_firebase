package com.sunil.internshala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchData extends AppCompatActivity {
private TextView ntitle,ndes;
private Button ndwon,nnext;
private int index=0;

List<Data> values=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);
        ntitle=findViewById(R.id.ntitle);
        ndes=findViewById(R.id.ndes);
        ndwon=findViewById(R.id.ndown);
        nnext=findViewById(R.id.nnext);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("pdfs");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    String title = childSnapshot.child("title").getValue(String.class);
                    String description = childSnapshot.child("description").getValue(String.class);
                    String url = childSnapshot.child("url").getValue(String.class);

                    Data data=new Data(title,description,url);
                    values.add(data);
                }
                loadData(index);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // now what to do with that array list




    }

    private void loadData(int index) {
        if(values.size()>index){
            Data data= values.get(index);
            ntitle.setText(data.getTitle());
            ndes.setText(data.getDes());
        }
    }

    public void downlaod(View view) {
        loadData(index);
        String url=values.get(index).getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void nextDownload(View view) {
        index=index+1;
        if (index < values.size()) {
            loadData(index);
        }else{
            Toast.makeText(this, "Last Data Item", Toast.LENGTH_SHORT).show();
        }
    }
}