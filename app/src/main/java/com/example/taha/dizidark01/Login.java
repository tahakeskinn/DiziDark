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

import com.example.taha.dizidark01.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    EditText emailEt, passEt;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        emailEt = findViewById(R.id.mailEt);
        passEt = findViewById(R.id.sifreEt);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
    }
    public void exitBtn(View view){
        Intent intent = new Intent(Login.this,Home.class);
        Login.this.finish();
        startActivity(intent);
    }
    public void signUp(View view){
        Intent intent = new Intent(Login.this,Register.class);
        Login.this.finish();
        startActivity(intent);
    }
    public void forgetBtn(View view){
        Intent intent = new Intent(Login.this,PassForget.class);
        finish();
        startActivity(intent);
    }
    public void signIn(View view){

        String email = emailEt.getText().toString();
        String pass = passEt.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(Login.this, "Alanlar boş bırakılarak giriş işlemi yapılamaz.",Toast.LENGTH_LONG).show();
        }
        else{
            if("admin".equals(email)){
                Intent intent = new Intent(Login.this, HomeAdmin.class);
                Login.this.finish();
                startActivity(intent);
            }
            else {
                login(email,pass);
            }
        }
    }

    private void login(final String email ,final String pass){
        auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            id = auth.getCurrentUser().getUid();

                            DatabaseReference newReference = firebaseDatabase.getReference("Users").child(id);
                            ValueEventListener listener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User user;
                                    user = dataSnapshot.getValue(User.class);

                                    loginPrefsEditor.putBoolean("hatirla", true);
                                    loginPrefsEditor.putString("id", user.getId());
                                    loginPrefsEditor.putString("username", user.getKullaniciadi());
                                    loginPrefsEditor.putString("name", user.getIsim());
                                    loginPrefsEditor.putString("email", email);
                                    loginPrefsEditor.putString("surname", user.getSoyad());
                                    loginPrefsEditor.putString("yetki", user.getYetki());
                                    loginPrefsEditor.putString("uphoto", user.getProfilePhoto());
                                    loginPrefsEditor.commit();


                                    if("user".equals(user.getYetki())){
                                        Intent intent = new Intent(Login.this,HomeUser.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else if("admin".equals(user.getYetki())){
                                        Intent intent = new Intent(Login.this,HomeAdmin.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(Login.this,"hatali yetki",Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };
                            newReference.addValueEventListener(listener);
                        }
                        else {
                            Toast.makeText(Login.this,"giriş yapılamadı. Bilgilerinizi kontrol edip tekrar deneyeniz", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
