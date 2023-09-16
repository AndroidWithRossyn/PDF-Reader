package com.qsa.ebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.qsa.ebooks.Adapters.BooksAdapter;
import com.qsa.ebooks.Model.Books;

import java.util.ArrayList;
import java.util.List;

public class CategoryResult extends AppCompatActivity {

    //recyclerView
    RecyclerView recyclerView;

    BooksAdapter booksAdapter;
    List<Books> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_result_adapter);

        recyclerView = findViewById(R.id.recycleView);

        Intent i = getIntent();

        //progressBar
        final ProgressDialog progressDialog = new ProgressDialog(CategoryResult.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        dataList = new ArrayList<>();
        booksAdapter = new BooksAdapter(CategoryResult.this, dataList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryResult.this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");

        Query query = ref.orderByChild("categoryName").equalTo(i.getStringExtra("name"));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                if (snapshot.getValue() != null) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        progressDialog.dismiss();
                        Books books = snapshot1.getValue(Books.class);
                        dataList.add(books);
                    }
                    recyclerView.setAdapter(booksAdapter);
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}