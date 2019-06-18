package com.example.taha.dizidark01.fragment_class;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.taha.dizidark01.Home;
import com.example.taha.dizidark01.R;
import com.example.taha.dizidark01.UserUpdate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.MODE_PRIVATE;

public class FragmentProfileInfo extends Fragment {
    Button exitBtn,updateBtn,passBtn;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    View view;
    FirebaseAuth auth;
    SharedPreferences preferences;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        view = lf.inflate(R.layout.fragment_profile_info, container, false);

        auth = FirebaseAuth.getInstance();

        preferences = getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        final String email = preferences.getString("email", "");

        updateBtn = view.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserUpdate.class);
                startActivity(intent);
            }
        });

        passBtn = view.findViewById(R.id.passBtn);
        passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPass(email);
            }
        });

        exitBtn = view.findViewById(R.id.cık);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPreferences= getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
                loginPrefsEditor = loginPreferences.edit();
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
                Intent intent = new Intent(getActivity(), Home.class);
                getActivity().finish();
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //VerileriAl();
        super.onActivityCreated(savedInstanceState);
    }

    private void sendPass(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),"Sifreniz E-mail Adresinize gönderilmiştir.",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(),"Boyle bir e-mail bulunmamaktadır.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
