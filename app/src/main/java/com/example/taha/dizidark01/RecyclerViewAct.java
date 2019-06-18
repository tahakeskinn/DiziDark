package com.example.taha.dizidark01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.taha.dizidark01.RecyclerView.SeriesClass;
import com.example.taha.dizidark01.RecyclerView.ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecyclerViewAct extends AppCompatActivity {
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        //recyclerView
        mRecyclerView = findViewById(R.id.RecyclerV);
        mRecyclerView.setHasFixedSize(true);
        //set layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //send query to FB
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Series");


        /*

        mAdapter = new AdapterRv(this,series);

        //recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        series = new ArrayList<>();
        recyclerView.setAdapter(mAdapter);
        getDataFromFirebase();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<SeriesClass, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<SeriesClass, ViewHolder>(
                        SeriesClass.class,
                        R.layout.row,
                        ViewHolder.class,
                        mRef
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, SeriesClass model, int position) {
                        viewHolder.setDetails(getApplicationContext(),model.getSeriesId(), model.getDiziAdi(),model.getImageUrl());
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    /*public void getDataFromFirebase(){
        DatabaseReference newReference = mFirebaseDatabase.getReference("Series");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    //series.add(new SeriesClass(hashMap.get("diziAdi"),hashMap.get("yapimYeri"),hashMap.get("yili"),hashMap.get("turu"),hashMap.get("konusu"),hashMap.get("adminPuani"),hashMap.get("adminYorum"),hashMap.get("imageUrl")));
                    SeriesClass dizi = ds.getValue(SeriesClass.class);
                    series.add(dizi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}
