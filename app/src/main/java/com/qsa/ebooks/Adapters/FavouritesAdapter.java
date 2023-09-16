package com.qsa.ebooks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qsa.ebooks.Fragments.Favourite_Fragment;
import com.qsa.ebooks.Interfaces.FragmentCallback;
import com.qsa.ebooks.Model.Books;
import com.qsa.ebooks.PdfDetail;
import com.qsa.ebooks.R;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.holder> {

    Context context;
    List<Books> dataList;
    FragmentCallback callback;

    public FavouritesAdapter(Context context, List<Books> dataList, FragmentCallback callback) {
        this.context = context;
        this.dataList = dataList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_list, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, final int position) {

        final Books books = dataList.get(position);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                        SharedPreferences sharedPreferences = context.getSharedPreferences("favourites", Context.MODE_PRIVATE);
                        boolean value = sharedPreferences.getBoolean("task_" + books.getId(), false);

                        if (value) {
                            Glide.with(context)
                                    .load(books.getBookImage())
                                    .into(holder.bookImage);
                            holder.heading.setText(books.getBookName());
                            holder.description.setText(books.getDescription());

                            holder.favBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferences sharedPreferences = context.getSharedPreferences("favourites", Context.MODE_PRIVATE);
                                    boolean check = sharedPreferences.getBoolean("task_" + books.getId(), false);

                                    if (check) {

                                        sharedPreferences = context.getSharedPreferences("favourites", context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putBoolean("task_" + books.getId(), false);
                                        editor.commit();

                                        callback.doSomething();
                                        //fragment.replace(R.id.fragment_container, fragment).commit();
                                        holder.favBtn.setImageResource(R.drawable.ic_favourite_border);
                                        Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show();

                                    } else {
                                        /*saveData(id);*/
                                        sharedPreferences = context.getSharedPreferences("favourites", context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putBoolean("task_" + books.getId(), true);
                                        editor.commit();

                                        holder.favBtn.setImageResource(R.drawable.ic_favourite);
                                        Toast.makeText(context, "Saved in favourites", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        }
    }
}
