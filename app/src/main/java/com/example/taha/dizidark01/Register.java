package com.example.taha.dizidark01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference reference;
    EditText nameEt, surnameEt,usernameEt,emailEt,passEt,passctrEt;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        nameEt = findViewById(R.id.nameEt);
        surnameEt = findViewById(R.id.surnameEt);
        usernameEt = findViewById(R.id.nicknameEt);
        emailEt = findViewById(R.id.emailEt);
        passEt = findViewById(R.id.passEt);
        passctrEt = findViewById(R.id.passctrlEt);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
    }
    public void exitBtn(View view){
        Intent intent = new Intent(Register.this,Home.class);
        Register.this.finish();
        startActivity(intent);
    }

    public void loginBtn(View view){
        Intent intent = new Intent(Register.this,Login.class);
        Register.this.finish();
        startActivity(intent);
    }

    public void signUp(View view){
        String name = nameEt.getText().toString();
        String surname = surnameEt.getText().toString();
        String username = usernameEt.getText().toString();
        String email = emailEt.getText().toString().trim();
        String pass = passEt.getText().toString();
        String passctrl = passctrEt.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(passctrl)){
            Toast.makeText(Register.this, "Alanlar boş bırakılarak kayıt işlemi yapılamaz.",Toast.LENGTH_LONG).show();
        }
        else if(!pass.equals(passctrl)){
            Toast.makeText(Register.this, "Sifre tekrarı yanlış girildi.",Toast.LENGTH_LONG).show();
        }
        else if(pass.length() < 6){
            Toast.makeText(Register.this, "Sifre 6 karakterden az olamaz.",Toast.LENGTH_LONG).show();
        }
        else{
            register(name,surname,username,email,pass);
        }
    }

    private void register(final String name, final String surname, final String username, final String email, final String pass){
        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            final String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                            final String resim = "https://firebasestorage.googleapis.com/v0/b/dizidark-f6063.appspot.com/o/users%2Fprofil.png?alt=media&token=ea134cd1-0c1c-4e3d-94bd-f854133235f5";
                            HashMap <String,Object> hashMap = new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("isim",name);
                            hashMap.put("soyad",surname);
                            hashMap.put("email",email);
                            hashMap.put("kullaniciadi",username);
                            hashMap.put("yetki","user");
                            hashMap.put("ProfilePhoto", resim);
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        loginPrefsEditor.putBoolean("hatirla", true);
                                        loginPrefsEditor.putString("id", userId);
                                        loginPrefsEditor.putString("username", username);
                                        loginPrefsEditor.putString("name", name);
                                        loginPrefsEditor.putString("email", email);
                                        loginPrefsEditor.putString("surname", surname);
                                        loginPrefsEditor.putString("yetki", "user");
                                        loginPrefsEditor.putString("uphoto", resim);
                                        loginPrefsEditor.commit();

                                        Intent intent = new Intent(Register.this,HomeUser.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(Register.this, "böyle bir e-mail kayıtlı ya da e-mail düzeninde hata bulunmaktadır", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

