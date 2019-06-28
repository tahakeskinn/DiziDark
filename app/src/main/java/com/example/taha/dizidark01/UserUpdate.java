package com.example.taha.dizidark01;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

public class UserUpdate extends AppCompatActivity {
    EditText nameEt, surnameEt,usernameEt,emailEt;
    ImageView imageView;
    SharedPreferences preferences;
    String name,uName,surname,id,yetki,uPhoto;
    Intent intent;
    Uri selectedImage,bosUri ;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        //findById
        nameEt = findViewById(R.id.nameEt);
        surnameEt = findViewById(R.id.surnameEt);
        usernameEt = findViewById(R.id.nicknameEt);
        emailEt = findViewById(R.id.emailEt);
        imageView = findViewById(R.id.imageView);

        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //prefence
        preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        name = preferences.getString("name", "");
        uName = preferences.getString("username", "");
        surname = preferences.getString("surname", "");
        id = preferences.getString("id", "");
        yetki = preferences.getString("yetki", "");
        uPhoto = preferences.getString("uphoto", "");

        //set value
        nameEt.setText(name);
        surnameEt.setText(surname);
        usernameEt.setText(uName);
        Picasso.get().load(uPhoto).into(imageView);

    }

    public void exitBtn(View view){
        if("admin".equals(yetki)){
            intent = new Intent(UserUpdate.this, HomeAdmin.class);}
        else{
            intent = new Intent(UserUpdate.this, HomeUser.class);}
        finish();
        startActivity(intent);
    }

    public void updateBtn(View view){
        String ad = nameEt.getText().toString();
        String soyad = surnameEt.getText().toString();
        String user = usernameEt.getText().toString();


        if(TextUtils.isEmpty(ad) || TextUtils.isEmpty(soyad) || TextUtils.isEmpty(user)){
            Toast.makeText(UserUpdate.this, "alanlar boş bırakılarak güncelleme yapılamaz.",Toast.LENGTH_LONG).show();
        }
        else{
            update(ad,soyad,user);
        }
    }

    public void update(final String name, final String surname, final String username){
        final String imageName = "users/"+id+".jpg";

        StorageReference sRef = storageReference.child(imageName);
        sRef.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final StorageReference newRef = FirebaseStorage.getInstance().getReference(imageName);
                newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadURL = uri.toString();

                        HashMap<String, Object> Data = new HashMap<>();
                        Data.put("id",id);
                        Data.put("isim",name);
                        Data.put("soyad",surname);
                        Data.put("kullaniciadi",username);
                        Data.put("yetki",yetki);
                        Data.put("ProfilePhoto",downloadURL);

                        reference.child("Users").child(id).setValue(Data);

                        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                        loginPrefsEditor = loginPreferences.edit();
                        loginPrefsEditor.putString("username", username);
                        loginPrefsEditor.putString("name", name);
                        loginPrefsEditor.putString("surname", surname);
                        loginPrefsEditor.putString("uphoto", downloadURL);
                        loginPrefsEditor.commit();

                        Toast.makeText(getApplicationContext(),"Bilgiler güncellendi.",Toast.LENGTH_LONG).show();
                    }
                });
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
