package com.example.taha.dizidark01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterAdmin extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference reference;
    private RadioGroup radioGroup;
    private RadioButton usr,admn;
    EditText nameEt, surnameEt,usernameEt,emailEt,passEt,passctrEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);

        auth = FirebaseAuth.getInstance();

        nameEt = findViewById(R.id.nameEt);
        surnameEt = findViewById(R.id.surnameEt);
        usernameEt = findViewById(R.id.nicknameEt);
        emailEt = findViewById(R.id.emailEt);
        passEt = findViewById(R.id.passEt);
        passctrEt = findViewById(R.id.passctrlEt);

        radioGroup = findViewById(R.id.usertype);
        usr = findViewById(R.id.usr);
        admn = findViewById(R.id.adm);
    }
    public void signUp(View view){

        int selectedId = radioGroup.getCheckedRadioButtonId();
        String name = nameEt.getText().toString();
        String surname = surnameEt.getText().toString();
        String username = usernameEt.getText().toString();
        String email = emailEt.getText().toString().trim();
        String pass = passEt.getText().toString();
        String passctrl = passctrEt.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(passctrl)){
            Toast.makeText(RegisterAdmin.this, "Alanlar boş bırakılarak kayıt işlemi yapılamaz.",Toast.LENGTH_LONG).show();
        }
        else if(!pass.equals(passctrl)){
            Toast.makeText(RegisterAdmin.this, "Sifre tekrarı yanlış girildi.",Toast.LENGTH_LONG).show();
        }
        else if(pass.length() < 6){
            Toast.makeText(RegisterAdmin.this, "Sifre 6 karakterden az olamaz.",Toast.LENGTH_LONG).show();
        }
        else{
            if(selectedId == admn.getId()) {
                /*radioGroup.clearCheck();
                nameEt.setText("");
                surnameEt.setText("");
                usernameEt.setText("");
                emailEt.setText("");
                passEt.setText("");
                passctrEt.setText("");*/
                register(name,surname,username,email,pass,"admin");
            }
            else if(selectedId == usr.getId()) {
                /*radioGroup.clearCheck(); alta ekle
                nameEt.setText("");
                surnameEt.setText("");
                usernameEt.setText("");
                emailEt.setText("");
                passEt.setText("");
                passctrEt.setText("");*/
                register(name,surname,username,email,pass,"user");
            }
            else {
                Toast.makeText(RegisterAdmin.this, "Kullanıcı tipi seçilmedi",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void exitBtn(View view){
        Intent intent = new Intent(RegisterAdmin.this,HomeAdmin.class);
        RegisterAdmin.this.finish();
        startActivity(intent);
    }
    private void register(final String name, final String surname, final String username, final String email, final String pass, final String yetki){
        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(RegisterAdmin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("isim",name);
                            hashMap.put("soyad",surname);
                            hashMap.put("kullaniciadi",username);
                            hashMap.put("email",email);
                            hashMap.put("sifre",pass);
                            hashMap.put("yetki",yetki);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterAdmin.this, yetki+" eklendi",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegisterAdmin.this,HomeUser.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(RegisterAdmin.this, "böyle bir e-mail kayıtlı ya da e-mail düzeninde hata bulunmaktadır", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
