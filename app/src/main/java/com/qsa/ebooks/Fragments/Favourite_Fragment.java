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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qsa.ebooks.Adapters.BooksAdapter;
import com.qsa.ebooks.Adapters.FavouritesAdapter;
import com.qsa.ebooks.Interfaces.FragmentCallback;
import com.qsa.ebooks.Model.Books;
import com.qsa.ebooks.R;

import java.util.ArrayList;
import java.util.List;

public class Favourite_Fragment extends Fragment implements FragmentCallback {

    RecyclerView recyclerView;
    List<Books> dataList;
    FavouritesAdapter favouritesAdapter;
    ProgressDialog progressDialog;
    TextView nothingText;

    SharedPreferences sharedPreferences;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourite_, container, false);

        recyclerView = view.findViewById(R.id.recycleViewFavourite);
        nothingText = view.findViewById(R.id.nothingTextFavourites);

        //progressBar
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        context = getContext();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //checking internet connectivity...
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network

            Favourite_Fragment fragment = new Favourite_Fragment();
            dataList = new ArrayList<>();
            favouritesAdapter = new FavouritesAdapter(getContext(), dataList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    if (snapshot.getValue() != null) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            progressDialog.dismiss();
                            Books books = snapshot1.getValue(Books.class);

                            sharedPreferences = context.getSharedPreferences("favourites", Context.MODE_PRIVATE);
                            boolean value = sharedPreferences.getBoolean("task_" + books.getId(), false);

                            if(value){
                                nothingText.setVisibility(View.GONE);
                                dataList.add(books);
                            }else{
                                nothingText.setVisibility(View.VISIBLE);
                            }
                        }
                        if(dataList.size()>0){
                            nothingText.setVisibility(View.GONE);

                        }else{
                            nothingText.setVisibility(View.VISIBLE);
                        }
                        recyclerView.setAdapter(favouritesAdapter);
                    }else{
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Download_Fragment()).commit();

        }

    }

    @Override
    public void doSomething() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Favourite_Fragment()).commit();
    }
}