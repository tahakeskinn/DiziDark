package com.example.taha.dizidark01.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taha.dizidark01.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

public class ViewHolder extends RecyclerView.ViewHolder {

    FirebaseDatabase firebaseDatabase;
    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition() );
            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v,getAdapterPosition());
                return true;
            }
        });
    }

    public void setDetails(Context ctx, final String  id, String title, String image ){
        TextView mTitleView = mView.findViewById(R.id.rTitleTv);
        TextView mSeriesIdView = mView.findViewById(R.id.rSeriesId);
        ImageView mImageView = mView.findViewById(R.id.rImageView);

        mTitleView.setText(title);
        mSeriesIdView.setText(id);
        Picasso.get().load(image).into(mImageView);

        ImageView removeIv = mView.findViewById(R.id.removeSeries);

        SharedPreferences preferences = mView.getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String kayit = preferences.getString("yetki", "");

        if("admin".equals(kayit)){
            removeIv.setVisibility(ImageView.VISIBLE);
        }

        removeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSeries(id);
            }
        });
    }
    public void setDetails(final Context ctx, final String  id){
        TextView mSeriesIdView = mView.findViewById(R.id.rSeriesId);

        seriesInfo(id);
        mSeriesIdView.setText(id);

        ImageView removeIv = mView.findViewById(R.id.removeSeries);

        removeIv.setVisibility(ImageView.VISIBLE);

        removeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFSeries(id);
            }
        });

    }

    private ViewHolder.ClickListener mClickListener;

    //interface
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }

    private void removeFSeries(String id){
        SharedPreferences preferences = mView.getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String uId = preferences.getString("id", "");
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference newReference = firebaseDatabase.getReference("Favorite").child(uId).child(id);
        newReference.removeValue();
    }

    private void removeSeries(String id){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference newReference = firebaseDatabase.getReference("Series").child(id);
        newReference.removeValue();
        //diziYorumları da silinsin diye
        removeComment(id);
    }

    private void removeComment(String sId){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference newReference = firebaseDatabase.getReference("Comments").child(sId);
        newReference.removeValue();
    }

    private void seriesInfo(String id){

        firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference newReference = firebaseDatabase.getReference("Series").child(id);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SeriesClass series;
                series = dataSnapshot.getValue(SeriesClass.class);


                TextView mTitleView = mView.findViewById(R.id.rTitleTv);
                ImageView mImageView = mView.findViewById(R.id.rImageView);

                mTitleView.setText(series.getDiziAdi());
                Picasso.get().load(series.imageUrl).into(mImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        newReference.addValueEventListener(listener);
    }
}
