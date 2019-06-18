package com.example.taha.dizidark01.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taha.dizidark01.Model.User;
import com.example.taha.dizidark01.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CommentHolder extends RecyclerView.ViewHolder {

    View mView;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    TextView cName;
    ImageView cImageView;

    public CommentHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setDetails(Context ctx,String  id, String comment){
        cName = mView.findViewById(R.id.cNameTv);//kullanıcı adi
        TextView cComment = mView.findViewById(R.id.cCommentTv);//comment
        cImageView = mView.findViewById(R.id.cImageTv);//resim

        getInfo(id);
        cComment.setText(comment);
    }

    private void getInfo(String id){
        firebaseDatabase= FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getInstance().getReference().child("Users").child(id);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user;
                user = dataSnapshot.getValue(User.class);

                cName.setText(user.getKullaniciadi());
                Picasso.get().load(user.getProfilePhoto()).into(cImageView);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addValueEventListener(listener);
    }
}
