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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qsa.ebooks.BookView;
import com.qsa.ebooks.Interfaces.FragmentCallback;
import com.qsa.ebooks.Model.Books;
import com.qsa.ebooks.R;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.holder> {

    Context context;
    List<Books> dataList;
    FragmentCallback callback;

    public BookmarkAdapter(Context context, List<Books> dataList, FragmentCallback callback) {
        this.context = context;
        this.dataList = dataList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.bookmark_list_item, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int position) {

        final Books books = dataList.get(position);
        holder.favBtn.setImageResource(R.drawable.ic_bookmark_filled);

      //  holder.bookImage.setBackgroundResource(R.drawable.booklayout);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = 0;
                String url = null;
                String totalPage;
                if (snapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                        SharedPreferences sharedPreferences = context.getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
                        value = sharedPreferences.getInt("pageNumber_" + books.getId(), 0);
                        url = sharedPreferences.getString("pdfUrl_" + books.getId(), "non");
                        totalPage = sharedPreferences.getString("totalPage_" + books.getId(), "non");


                        if (value != 0) {
                            Glide.with(context)
                                    .load(books.getBookImage())
                                    .into(holder.bookImage);
                            holder.heading.setText(books.getBookName());
                            holder.categoryText.setText(books.getCategoryName());
                            holder.pageNo.setText(String.valueOf(value));
                            holder.totalPageNo.setText("/" + totalPage);

                            holder.favBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferences sharedPreferences = context.getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
                                    int check = sharedPreferences.getInt("pageNumber_" + books.getId(), 0);
                                    Boolean clicked = sharedPreferences.getBoolean("pageClicked_" + books.getId(), false);

                                    if (check != 0) {

                                        sharedPreferences = context.getSharedPreferences("bookmarks", context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putInt("pageNumber_" + books.getId(), 0);
                                        editor.putBoolean("pageClicked_" + books.getId(),false);
                                        editor.commit();

                                        callback.doSomething();
                                        Toast.makeText(context, "Removed from bookmarks", Toast.LENGTH_SHORT).show();

                                    } else {

                                        holder.favBtn.setImageResource(R.drawable.ic_bookmark_filled);
                                        Toast.makeText(context, "Saved in bookmarks", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }


                    }
                }

                value --;
                final String finalValue = String.valueOf(value);
                final String finalUrl = url;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, BookView.class);
                        i.putExtra("pageNumber", finalValue);
                        i.putExtra("identity", "bookmarks");
                        i.putExtra("bookImage", books.getBookImage());
                        i.putExtra("bookId", books.getId());
                        i.putExtra("pdfUrl", finalUrl);
                        context.startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        TextView categoryText;
        TextView pageNo;
        TextView totalPageNo;

        public holder(@NonNull View itemView) {
            super(itemView);

            favBtn = itemView.findViewById(R.id.favBtn);
            heading = itemView.findViewById(R.id.text1);
            categoryText = itemView.findViewById(R.id.categoryText);
            bookImage = itemView.findViewById(R.id.bookImageFavourite);
            pageNo = itemView.findViewById(R.id.pageNoText);
            totalPageNo = itemView.findViewById(R.id.totalPage);

        }
    }
}
