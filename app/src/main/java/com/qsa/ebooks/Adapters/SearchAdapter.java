package com.qsa.ebooks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qsa.ebooks.Interfaces.FragmentCallback;
import com.qsa.ebooks.Model.Books;
import com.qsa.ebooks.PdfDetail;
import com.qsa.ebooks.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.holder> {

    Context context;
    List<Books> dataList;

    public SearchAdapter(Context context, List<Books> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SearchAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_list, parent, false);
        return new SearchAdapter.holder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.holder holder, int position) {

        final Books books = dataList.get(position);
                            Glide.with(context)
                                    .load(books.getBookImage())
                                    .into(holder.bookImage);
                            holder.heading.setText(books.getBookName());
                            holder.description.setText(books.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PdfDetail.class);
                i.putExtra("bookImage", books.getBookImage());
                i.putExtra("bookName", books.getBookName());
                i.putExtra("authorName", books.getAuthorName());
                i.putExtra("description", books.getDescription());
                i.putExtra("pdfUrl", books.getPdfUrl());
                i.putExtra("id", books.getId());
                i.putExtra("bookDownloads", books.getDownloads());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        ImageView favBtn;
        ImageView bookImage;
        TextView heading;
        TextView description;
        public holder(@NonNull View itemView) {
            super(itemView);

            favBtn = itemView.findViewById(R.id.favBtn);
            heading = itemView.findViewById(R.id.text1);
            description = itemView.findViewById(R.id.text2);
            bookImage = itemView.findViewById(R.id.bookImageFavourite);

            favBtn.setVisibility(View.GONE);
        }
    }
}
