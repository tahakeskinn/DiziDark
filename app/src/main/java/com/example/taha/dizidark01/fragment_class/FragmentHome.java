package com.example.taha.dizidark01.fragment_class;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taha.dizidark01.R;
import com.example.taha.dizidark01.RecyclerView.SeriesClass;
import com.example.taha.dizidark01.RecyclerView.ViewHolder;
import com.example.taha.dizidark01.SeriesDetailActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class FragmentHome extends Fragment {
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);

        //GridLayout
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        //recyclerView
        mRecyclerView = view.findViewById(R.id.RecyclerV);
        mRecyclerView.setHasFixedSize(true);
        //set layout
        mRecyclerView.setLayoutManager(layoutManager);
        //send query to FB
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Series");



        return view;
    }


    @Override
    public void onStart() {
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
                        viewHolder.setDetails(getContext(),model.getSeriesId(), model.getDiziAdi().toUpperCase(),model.getImageUrl());}


                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView mTitleTv = view.findViewById(R.id.rTitleTv);
                                TextView mSeriesIdTv = view.findViewById(R.id.rSeriesId);
                                ImageView mImageIv = view.findViewById(R.id.rImageView);

                                String mTitle = mTitleTv.getText().toString();
                                String mSeriesId = mSeriesIdTv.getText().toString();
                                Drawable mDrawable = mImageIv.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

                                Intent intent = new Intent(view.getContext(), SeriesDetailActivity.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                                byte[] bytes =stream.toByteArray();
                                intent.putExtra("image",bytes);
                                intent.putExtra("name",mTitle);
                                intent.putExtra("seriesId",mSeriesId);
                                startActivity(intent);

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });

                        return viewHolder;
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
