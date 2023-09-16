package com.qsa.ebooks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qsa.ebooks.CategoryResult;
import com.qsa.ebooks.Model.Categories;
import com.qsa.ebooks.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.holder> {

    Context context;
    List<Categories> dataList;

    public CategoryAdapter(Context context, List<Categories> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.categorylisticon, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        final Categories cat = dataList.get(position);

        Glide.with(context)
                .load(cat.getCategoryImage())
                .into(holder.imageView);

        holder.textView.setText(cat.getCategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(), CategoryResult.class);
                i.putExtra("uri", cat.getCategoryImage());
                i.putExtra("name", cat.getCategoryName());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView textView;

        public holder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.itemImage);
            textView = itemView.findViewById(R.id.itemText);

        }
    }
}
