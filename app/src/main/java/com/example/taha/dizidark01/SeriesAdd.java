package com.example.taha.dizidark01;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class SeriesAdd extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    EditText diziAdi, yapimYeri,bYili,dTuru,konu,aPuan,aYorum;
    ImageView imageView;
    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_add);


        diziAdi = findViewById(R.id.seriesName);
        yapimYeri = findViewById(R.id.place);
        bYili = findViewById(R.id.date);
        dTuru = findViewById(R.id.kind);
        konu = findViewById(R.id.topic);
        aPuan = findViewById(R.id.aPoint);
        aYorum = findViewById(R.id.aComment);

        imageView = findViewById(R.id.selectIv);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void addBtn(View view){
        String name = diziAdi.getText().toString();
        String place = yapimYeri.getText().toString();
        String date = bYili.getText().toString();
        String kind = konu.getText().toString();
        String topic = konu.getText().toString().trim();
        String point = aPuan.getText().toString();
        String comment = aYorum.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(place) || TextUtils.isEmpty(date) || TextUtils.isEmpty(kind) || TextUtils.isEmpty(topic) || TextUtils.isEmpty(point)|| TextUtils.isEmpty(comment)){
            Toast.makeText(SeriesAdd.this, "Alanlar boş bırakılarak kayıt işlemi yapılamaz.",Toast.LENGTH_LONG).show();
        }
        else{
            addSeries(name,place,date,kind,topic,point,comment);
        }
    }

    public void exitBtn(View view){
        Intent intent = new Intent(SeriesAdd.this, HomeAdmin.class);
        SeriesAdd.this.finish();
        startActivity(intent);
    }

    public void addSeries(final String name, final String place, final String date, final String kind, final String topic, final String point, final String comment){
        UUID uuid = UUID.randomUUID();
        final String imageName = "images/"+uuid+".jpg";

        StorageReference sRef = storageReference.child(imageName);
        sRef.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final StorageReference newRef = FirebaseStorage.getInstance().getReference(imageName);
                newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadURL = uri.toString();

                        UUID uuid1 = UUID.randomUUID();
                        String uuidString = uuid1.toString();

                        HashMap<String, Object> Data = new HashMap<>();
                        Data.put("diziAdi",name);
                        Data.put("seriesId",uuidString);
                        Data.put("yapimYeri",place);
                        Data.put("turu",kind);
                        Data.put("yapimYili",date);
                        Data.put("konusu",topic);
                        Data.put("adminPuani",point);
                        Data.put("adminYorum",comment);
                        Data.put("imageUrl",downloadURL);

                        reference.child("Series").child(uuidString).setValue(Data);

                        Toast.makeText(getApplicationContext(),"dizi eklendi",Toast.LENGTH_LONG).show();

                    }
                });
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void Select(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }
        else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
