package com.zercey.paint.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zercey.paint.R;
import com.zercey.paint.holders.ImageViewHolder;
import com.zercey.paint.model.UserImage;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<UserImage> images;
    private Context context;


    public ImageAdapter(List<UserImage> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        UserImage image = images.get(position);

        Picasso.get().load(image.getImage()).error(R.drawable.ic_image_black_24dp).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}

