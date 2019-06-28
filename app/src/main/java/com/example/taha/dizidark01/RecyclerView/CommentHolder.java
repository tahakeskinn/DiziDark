package com.example.taha.dizidark01.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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

import static android.content.Context.MODE_PRIVATE;

public class CommentHolder extends RecyclerView.ViewHolder {

    View mView;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    TextView cName;
    ImageView cImageView;
    CardView adminView;

    public CommentHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setDetails(Context ctx, final String  uId, String comment, final String sId, final String cId){
        cName = mView.findViewById(R.id.cNameTv);//kullanıcı adi
        TextView cComment = mView.findViewById(R.id.cCommentTv);//comment
        cImageView = mView.findViewById(R.id.cImageTv);//resim

        getInfo(uId);
        cComment.setText(comment);

        ImageView removeIv = mView.findViewById(R.id.removeComment);
        adminView = mView.findViewById(R.id.adminCv);

        SharedPreferences preferences = mView.getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String kayit = preferences.getString("yetki", "");
        String cUId = preferences.getString("id", "");

        if("admin".equals(kayit)){
            removeIv.setVisibility(ImageView.VISIBLE);
        }
        else if(uId.equals(cUId)){
            removeIv.setVisibility(ImageView.VISIBLE);
        }

        removeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeComment(cId,sId);
            }
        });
    }

    private void removeComment(String cId,String sId){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference newReference = firebaseDatabase.getReference("Comments").child(sId).child(cId);
        newReference.removeValue();
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

                if("admin".equals(user.getYetki())){
                    adminView.setVisibility(ImageView.VISIBLE);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addValueEventListener(listener);
    }
}
