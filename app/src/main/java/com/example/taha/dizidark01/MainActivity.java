package com.example.taha.dizidark01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        final String kayit = preferences.getString("yetki", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if("user".equals(kayit)){
                    Intent intent = new Intent(MainActivity.this, HomeUser.class);
                    MainActivity.this.finish();
                    startActivity(intent);
                }
                else if ("admin".equals(kayit)) {
                    Intent intent = new Intent(MainActivity.this, HomeAdmin.class);
                    MainActivity.this.finish();
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                    MainActivity.this.finish();

                }
                    }
        },3000);
    }
}
