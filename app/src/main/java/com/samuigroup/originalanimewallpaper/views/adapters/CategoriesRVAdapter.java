package com.samuigroup.originalanimewallpaper.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.models.entities.Category;
import com.samuigroup.originalanimewallpaper.views.SearchImagesActivity;

import java.util.ArrayList;

public class CategoriesRVAdapter extends RecyclerView.Adapter<CategoriesRVAdapter.ConstraintViewHolder> {
    protected class ConstraintViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon;
        private TextView txtName;
        private TextView txtDescription;
        public ConstraintViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDescription = itemView.findViewById(R.id.txt_description);
        }
    }

    private Context context;
    private ArrayList<Category> categories;

    public CategoriesRVAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ConstraintViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.category, viewGroup, false);
        return new ConstraintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConstraintViewHolder constraintViewHolder, int i) {
        final Category category = categories.get(i);
        Glide
                .with(context)
                .load(category.getIconUrl())
                .into(constraintViewHolder.ivIcon);
        constraintViewHolder.txtName.setText(category.getName());
        constraintViewHolder.txtDescription.setText(category.getDescription());
        ((View)(constraintViewHolder.ivIcon.getParent())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchImagesActivity.class);
                intent.putExtra("tags", new String[] {category.getTag()});
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
