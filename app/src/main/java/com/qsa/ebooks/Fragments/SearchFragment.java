package com.qsa.ebooks.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.qsa.ebooks.Adapters.SearchAdapter;
import com.qsa.ebooks.Model.Books;
import com.qsa.ebooks.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<Books> mBooks;
    EditText search_bar;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        search_bar= view.findViewById(R.id.search_bar);
        mBooks = new ArrayList<>();
        searchAdapter = new SearchAdapter(getContext(), mBooks);
        //readPosts();
        recyclerView.setAdapter(searchAdapter);

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String word = s.toString();
                searchPosts(word.toUpperCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void searchPosts(String s){
        Query query = FirebaseDatabase.getInstance().getReference("Books").orderByChild("bookName")
                .startAt(s.toUpperCase()).endAt(s.toUpperCase()+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("searchfrag", "onDataChange: 1"+mBooks+ "  ccc :"+snapshot);
                mBooks.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Books books = snapshot1.getValue(Books.class);
                    mBooks.add(books);
                    Log.i("searchfrag", "onDataChange: 2"+mBooks+ "  ccc :"+snapshot);
                }
                searchAdapter.notifyDataSetChanged();
                Log.i("searchfrag", "onDataChange: 3"+mBooks+ "  ccc :"+snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Books");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (search_bar.getText().toString().equals("")){
                    mBooks.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        Books books = snapshot1.getValue(Books.class);
                        mBooks.add(books);



                        Log.i("searchfrag", "onDataChange: "+mBooks+ "  ccc :"+snapshot);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}