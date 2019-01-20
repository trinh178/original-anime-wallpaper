package com.samuigroup.originalanimewallpaper.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.models.entities.Post;
import com.samuigroup.originalanimewallpaper.views.PostActivity;

import java.util.ArrayList;

public class PostsRVAdapter extends RecyclerView.Adapter<PostsRVAdapter.ContraintViewHolder> {
    protected class ContraintViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPreview;

        public ContraintViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPreview = itemView.findViewById(R.id.iv_preview);
        }
    }

    // Properties
    private Context context;
    private ArrayList<Post> posts;

    // Contructors
    public PostsRVAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ContraintViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.preview_post, null, false);
        return new ContraintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContraintViewHolder contraintViewHolder, int i) {
        final Post post = posts.get(i);
        Glide
                .with(context)
                .load(post.getPreviewUrl())
                .into(contraintViewHolder.ivPreview);

        // Event click
        contraintViewHolder.ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra("post", post);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
