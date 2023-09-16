package com.qsa.ebooks;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.qsa.ebooks.Fragments.Bookmark_Fragment;
import com.qsa.ebooks.Fragments.Category_Fragment;
import com.qsa.ebooks.Fragments.Download_Fragment;
import com.qsa.ebooks.Fragments.Favourite_Fragment;
import com.qsa.ebooks.Fragments.Home_Fragment;
import com.google.android.material.navigation.NavigationView;
import com.qsa.ebooks.Fragments.SearchFragment;
import com.qsa.ebooks.Interfaces.FragmentCallback;


public class MainActivity extends AppCompatActivity {

    public static Myappdatabas myappdatabas;
    private DrawerLayout drawer;
    MeowBottomNavigation meowBottomNavigation;
    NavigationView navigationView;
    Fragment selectedFragment = null;
    MeowBottomNavigation.Model item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        myappdatabas = Room.databaseBuilder(getApplicationContext(),Myappdatabas.class,"userdb").allowMainThreadQueries().build();

        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        /*TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_text);
        mTitle.setText("Ebooks");*/

        meowBottomNavigation = findViewById(R.id.bottom_nav);
        MeowBottomNavigation.ReselectListener reselectListener = new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        };
        meowBottomNavigation.setOnReselectListener(reselectListener);

        //navigationView = findViewById(R.id.nav_view1);
        //navigationView.setNavigationItemSelectedListener(getNavListenerview);

        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_category));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_bookmark_filled));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_homeicon));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_favourite));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_download));

        if (selectedFragment == null) {
            selectedFragment = new Home_Fragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();



        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                if (item.getId() == 1) {
                    selectedFragment = new Category_Fragment();
                    toolbar.setTitle("Category");
                } else if (item.getId() == 2) {
                    selectedFragment = new Bookmark_Fragment();
                    toolbar.setTitle("Bookmark");
                } else if (item.getId() == 3) {
                    selectedFragment = new Home_Fragment();
                    toolbar.setTitle("Home");
                } else if (item.getId() == 4) {
                    selectedFragment = new Favourite_Fragment();
                    toolbar.setTitle("Favourites");
                } else if (item.getId() == 5) {
                    selectedFragment = new Download_Fragment();
                    toolbar.setTitle("Downloads");
                } else {
                    selectedFragment = new Home_Fragment();
                    toolbar.setTitle("Home");
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            }
        });

        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {


                if (item.getId() != 1 && item.getId() != 2 && item.getId() != 3 && item.getId() != 4 && item.getId() != 5) {
                    meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_homeicon));
                }

            }
        });

    }
    /*private NavigationView.OnNavigationItemSelectedListener getNavListenerview = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {


                case R.id.nav_home:
                    selectedFragment = new Home_Fragment();
                    meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_homeicon));
                    break;

                case R.id.nav_category:
                    selectedFragment = new Category_Fragment();
                    meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_category));
                    break;

                case R.id.nav_share:
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "https://play.google.com/store/apps/details?id=com.dewmobile.kuaiya.play&hl=en";
                    String shareSubject = "Shareable link";

                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);

                    startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                    break;

                case R.id.nav_rate:
                    //paste this: "market://details?id=" + getPackageName() , inside Uri.parse(here) when your app is live on google play.
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.dewmobile.kuaiya.play&hl=en");
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(myAppLinkToMarket);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, " unable to find your app", Toast.LENGTH_LONG).show();
                    }
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    };*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuitem = menu.findItem(R.id.action_search);
        menuitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).addToBackStack(null).commit();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if (!(selectedFragment instanceof Home_Fragment)) {

            meowBottomNavigation.setSelected(true);
            Log.i("bottomMenu: ", "onBackPressed: " + meowBottomNavigation.getId());
            if(meowBottomNavigation.getId() != 3){
                meowBottomNavigation.setId(3);
                selectedFragment = new Home_Fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return;
        }

        /*if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }*/
        super.onBackPressed();
    }
}