package com.example.taha.dizidark01.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taha.dizidark01.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

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

    public void setDetails(Context ctx,String  id, String title, String image ){
        TextView mTitleView = mView.findViewById(R.id.rTitleTv);
        TextView mSeriesIdView = mView.findViewById(R.id.rSeriesId);
        ImageView mImageView = mView.findViewById(R.id.rImageView);

        mTitleView.setText(title);
        mSeriesIdView.setText(id);
        Picasso.get().load(image).into(mImageView);
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
}
