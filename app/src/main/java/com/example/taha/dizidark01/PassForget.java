package com.example.taha.dizidark01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PassForget extends AppCompatActivity {
    EditText emailEt;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_forget);

        emailEt = findViewById(R.id.mailEt);
        auth = FirebaseAuth.getInstance();

    }
    public void exitBtn(View view){
        Intent intent = new Intent(PassForget.this,Home.class);
        finish();
        startActivity(intent);
    }
    public void sendBtn(View view){
        String email = emailEt.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(PassForget.this, "Alan boş bırakılamaz.",Toast.LENGTH_LONG).show();
        }
        else {
            sendPass(email);
        }
    }
    private void sendPass(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PassForget.this,"Sifreniz E-mail Adresinize gönderilmiştir.",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(PassForget.this,"Boyle bir e-mail bulunmamaktadır.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
