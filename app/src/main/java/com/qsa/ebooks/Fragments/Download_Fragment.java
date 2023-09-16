package com.qsa.ebooks.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.qsa.ebooks.Adapters.BookmarkAdapter;
import com.qsa.ebooks.Adapters.DownloadsAdapter;
import com.qsa.ebooks.Booksdb;
import com.qsa.ebooks.Interfaces.FragmentCallback;
import com.qsa.ebooks.MainActivity;
import com.qsa.ebooks.Model.Books;
import com.qsa.ebooks.PdfDetail;
import com.qsa.ebooks.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Download_Fragment extends Fragment implements FragmentCallback {

    RecyclerView recyclerView;
    ArrayList<Booksdb> dataList;
    DownloadsAdapter downloadAdapter;
    ProgressDialog progressDialog;
    public List<Booksdb> booksdbs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_download_, container, false);

        recyclerView = view.findViewById(R.id.recycleViewDownloads);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        //Favourite_Fragment fragment = new Favourite_Fragment();
        booksdbs = MainActivity.myappdatabas.myDao().getBooks();
        loadData();
        downloadAdapter = new DownloadsAdapter(getContext(), dataList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(downloadAdapter);


    }

    @Override
    public void doSomething() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Bookmark_Fragment()).commit();
    }

    private void loadData() {
/*
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("saveData", getContext().MODE_PRIVATE);
        int dataListSize = sharedPreferences.getInt("dataListSize", 0);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Books>>() {
        }.getType();



*/

        String info = "";
        dataList = new ArrayList<>();
        dataList.clear();

        for (Booksdb booksd : booksdbs)
        {
            dataList.add(booksd);
            String id = booksd.getId();
            String bookName = booksd.getBookName();
            String booksdesc = booksd.getDescription();
            info = info+"\n\n"+"id: "+id+"\n bookname: "+bookName+"\n bookcategory: "+booksdesc;
        }

        Log.e("TAGDATA", info);


        /*for (int i = 0; i < dataListSize; i++) {
            try {
                Log.i("Order is ", "" + dataListSize); // Log the order
                String json = sharedPreferences.getString("downloadBookData_" + dataList.get(i).getId(), null);
                dataList = gson.fromJson(json, type);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }*/



    }
}