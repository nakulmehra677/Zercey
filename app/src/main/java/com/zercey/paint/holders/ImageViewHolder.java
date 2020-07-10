package com.zercey.paint.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zercey.paint.R;

public class ImageViewHolder extends RecyclerView.ViewHolder {

    public ImageView image;

    public ImageViewHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.image);
    }
}