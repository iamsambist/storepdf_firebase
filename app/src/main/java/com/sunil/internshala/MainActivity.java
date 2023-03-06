package com.sunil.internshala;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
private AppCompatEditText mtitle,mdes;
private ImageButton mimg;
private TextView mgett;
private Button mupload;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PICK_PDF_REQUEST_CODE = 2;

    private Uri selectedFileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtitle=findViewById(R.id.mtitle);
        mdes=findViewById(R.id.mdes);
        mimg=findViewById(R.id.mimgbutton);
        mgett=findViewById(R.id.mgett);
        mupload=findViewById(R.id.mupload);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // We don't have the permission, request it
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }

        mimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(checkStoragePermission()){
                   uploadData();
               } else{
                   Toast.makeText(MainActivity.this, "Give Permission", Toast.LENGTH_SHORT).show();
               }
            }
        });

        mupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAllConstraints()){
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference().
                            child("pdfs/" + UUID.randomUUID().toString() + ".pdf");

                    storageRef.putFile(selectedFileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Create a new PdfFile object with the title, description, and PDF download URL
                                    makefile pdfFile = new makefile(mtitle.getText().toString(),
                                            mdes.getText().toString(), uri.toString());

                                    // Save the PdfFile object to the Firebase Realtime Database
                                    DatabaseReference pdfRef = FirebaseDatabase.getInstance().getReference("pdfs").push();
                                    pdfRef.setValue(pdfFile);

                                    // Display a success message to the user
                                    Toast.makeText(MainActivity.this, "PDF file uploaded successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }


// mupload bracket
            }
        });

    }

    private boolean checkAllConstraints() {
        if (TextUtils.isEmpty(mtitle.getText().toString()) ||
                TextUtils.isEmpty(mdes.getText().toString()) ||
                TextUtils.isEmpty(mgett.getText().toString())){
            Toast.makeText(this, "fill All the field", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkStoragePermission() {
        // Cheking permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // We don't have the permission, request it
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {
            // We have the permission, start the file picker activity
            return true;
        }
        return false;
    }

    private void uploadData() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        try{
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST_CODE);
        } catch (Exception e){
            Log.i("msg","nothing inside phone");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST_CODE  && data != null) {
            selectedFileUri = data.getData();

            // Upload the selected file
            uploadFile(selectedFileUri);
        }
    }

    private void uploadFile(Uri selectedFileUri) {
        ContentResolver contentResolver = getContentResolver();
        // getting title of selected pdf
        Cursor cursor = contentResolver.query(selectedFileUri, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String title = cursor.getString(nameIndex);
        cursor.close();
        if (title.length() > 10) {
            title = title.substring(0, 10) + "..";
        }

        // TODO: Upload the file to your server
        Toast.makeText(MainActivity.this, "Click Upload ", Toast.LENGTH_LONG).show();
        mgett.setVisibility(View.VISIBLE);
        mgett.setText(title);

    }


}