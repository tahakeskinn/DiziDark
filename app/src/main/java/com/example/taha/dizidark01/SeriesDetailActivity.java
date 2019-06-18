package com.example.taha.dizidark01;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taha.dizidark01.Model.Comment;
import com.example.taha.dizidark01.Model.Series;
import com.example.taha.dizidark01.RecyclerView.CommentHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.UUID;

public class SeriesDetailActivity extends AppCompatActivity {
    TextView name, topic,place,date,kind,imdb,comment;
    ImageView png,favori;
    String uName,uId,yetki;
    Button commentBtn;
    String id;

    DatabaseReference reference;
    SharedPreferences preferences;
    RecyclerView mRecyclerView;

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);

        firebaseDatabase= FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        name = findViewById(R.id.TitleTv);
        png = findViewById(R.id.ImageView);
        favori = findViewById(R.id.favorite);
        place = findViewById(R.id.ulkeTv);
        date = findViewById(R.id.yılTv);
        kind = findViewById(R.id.turTv);
        imdb = findViewById(R.id.imdbTv);
        topic = findViewById(R.id.TopicTv);
        comment = findViewById(R.id.yorumTv);
        commentBtn = findViewById(R.id.sendComment);
        commentBtn = findViewById(R.id.sendComment);

        byte[] bytes = getIntent().getByteArrayExtra("image");
        String title = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("seriesId");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length );

        name.setText(title);
        png.setImageBitmap(bmp);
        seriesInfo(id);

        //prefence
        preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        uName = preferences.getString("username", "");
        uId = preferences.getString("id", "");
        yetki = preferences.getString("yetki", "");

        if(!"".equals(yetki)){
            favori.setVisibility(ImageView.VISIBLE);
            comment.setVisibility(TextView.VISIBLE);
            commentBtn.setVisibility(Button.VISIBLE);
        }

        //recyclerView
        mRecyclerView = findViewById(R.id.cRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        //set layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference mRef = firebaseDatabase.getReference("Comments").child(id);
        FirebaseRecyclerAdapter<Comment, CommentHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                        Comment.class,
                        R.layout.comment_row,
                        CommentHolder.class,
                        mRef
                ) {
                    @Override
                    protected void populateViewHolder(CommentHolder viewHolder, Comment model, int position) {
                        viewHolder.setDetails(getApplicationContext(),model.getUserId(),model.getYorum());
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public void commentBtn(View view){

        String yorum =comment.getText().toString();
        if(TextUtils.isEmpty(yorum)){
            Toast.makeText(this,"yorum satırı boş bırakılamaz.",Toast.LENGTH_LONG).show();
        }
        else {
            addComment(yorum);
        }
    }

    private void addComment(String commentStr){

        UUID uuid1 = UUID.randomUUID();
        String commentId = uuid1.toString();

        HashMap<String, Object> Data = new HashMap<>();
        Data.put("userId",uId);
        Data.put("yorum",commentStr);
        //Data.put("ProfilePhoto",downloadURL);//profil foto link

        reference.child("Comments").child(id).child(commentId).setValue(Data);

        comment.setText("");
        Toast.makeText(this,"yorum eklendi.",Toast.LENGTH_LONG).show();
    }



    private void seriesInfo(String id){
        DatabaseReference newReference = firebaseDatabase.getReference("Series").child(id);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Series series;
                series = dataSnapshot.getValue(Series.class);

                imdb.setText(series.getAdminPuani());
                place.setText(series.getYapimYeri());
                date.setText(series.getYapimYili());
                kind.setText("# " + series.getTuru());
                topic.setText(series.getKonusu());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        newReference.addValueEventListener(listener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
