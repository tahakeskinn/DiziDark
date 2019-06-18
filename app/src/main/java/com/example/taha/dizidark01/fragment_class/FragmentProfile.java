package com.example.taha.dizidark01.fragment_class;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.taha.dizidark01.Login;
import com.example.taha.dizidark01.R;
import com.example.taha.dizidark01.Register;

public class FragmentProfile extends Fragment {

    Button girisyap;
    Button kayitol;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        girisyap = view.findViewById(R.id.login);
        kayitol = view.findViewById(R.id.register);

        girisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Login.class);
                getActivity().finish();
                startActivity(intent);

            }
        });

        kayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Register.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
