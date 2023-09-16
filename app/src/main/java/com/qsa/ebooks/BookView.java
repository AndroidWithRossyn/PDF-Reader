package com.qsa.ebooks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.cleveroad.sy.cyclemenuwidget.CycleMenuWidget;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.qsa.ebooks.Model.Books;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;

public class BookView extends AppCompatActivity implements OnLoadCompleteListener, OnPageChangeListener, OnPageErrorListener {

    private static final String TAG = "bookmarked";
    CycleMenuWidget cycleMenuWidget;


    private static final int WRITE_EXT_STORAGECODE = 1;
    PDFView pdfView;
    ProgressDialog progressDialog;
    Context context;
    MediaPlayer mp;
    String id1;
    String identity = "";
    String identity2 = "";
    String bookImageUrl;
    int pageNumber = 0;
    String pageNo = "";
    int pageNo2 = 0;
    int pageNoRecreate = 0;
    String url;
    OnTapListener onTapListener;

    String fileDirectory;
    boolean nightMode;

    //initialization of file paths in storage
    File pdfUrl;
    String totalPage;
    String bookName;

    private NavigationView.OnNavigationItemSelectedListener navListenerview;
    BottomNavigationView bottomNav;
    private boolean tapped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookview);

        loadBannerAd();


        bottomNav = findViewById(R.id.bottom_navigation_bookView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Intent i = getIntent();
        url = i.getStringExtra("pdfUrl");
        id1 = i.getStringExtra("bookId");
        identity = i.getStringExtra("identity");
        bookImageUrl = i.getStringExtra("bookImage");
        pageNo = i.getStringExtra("pageNumber");
        bookName = i.getStringExtra("bookName");


        SharedPreferences sharedPreferences = getSharedPreferences("bookmarks", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightMode", false);
        identity2 = sharedPreferences.getString("identity", "");
        pageNoRecreate = sharedPreferences.getInt("page", -1);

        if (pageNoRecreate >= 0) {
            pageNo2 = pageNoRecreate;
        }


        if (pageNo != null) {
            pageNo2 = Integer.parseInt(pageNo);
        }

        /*cycleMenuWidget = findViewById(R.id.itemCycleMenuWidget);
        cycleMenuWidget.setOnMenuItemClickListener(this);
        cycleMenuWidget.setMenuRes(R.menu.cycle_menu_items);
        cycleMenuWidget.setCorner(CycleMenuWidget.CORNER.RIGHT_BOTTOM);*/

        pdfView = findViewById(R.id.pdfView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait\nFetching pdf...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        onTapListener = new OnTapListener() {
            @Override
            public boolean onTap(MotionEvent e) {
                if (tapped) {
                    bottomNav.setVisibility(View.INVISIBLE);
                    //pdfView.setNestedScrollingEnabled(false);
                    tapped = false;
                } else {
                    //pdfView.setNestedScrollingEnabled(true);
                    bottomNav.setVisibility(View.VISIBLE);
                    tapped = true;
                }
                return true;
            }
        };


        FileLoader.with(this)
                .load(url, false) //2nd parameter is optioal, pass true to force load from network
                .fromDirectory("test4", FileLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        pdfUrl = response.getBody();
                        // do something with the file


                        if ("bookmarks".equals(identity) || "itself".equals(identity2)) {

                            try {

                                pdfView.fromFile(pdfUrl)
                                        .onTap(onTapListener)
                                        .onPageScroll(new OnPageScrollListener() {
                                            @Override
                                            public void onPageScrolled(int page, float positionOffset) {
                                                bottomNav.setVisibility(View.VISIBLE);

                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        bottomNav.setVisibility(View.INVISIBLE);
                                                    }
                                                }, 1500);
                                            }
                                        })
                                        .defaultPage(pageNo2)
                                        .enableSwipe(true)
                                        .enableAnnotationRendering(true)
                                        .onLoad(BookView.this)
                                        .onPageChange(BookView.this)
                                        .scrollHandle(new DefaultScrollHandle(BookView.this))
                                        .enableDoubletap(true)
                                        .onPageError(BookView.this)
                                        .swipeHorizontal(true)
                                        .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                                        .fitEachPage(true)
                                        .pageSnap(true)
                                        .pageFling(true)
                                        .spacing(0)
                                        .nightMode(nightMode)
                                        .load();

                                SharedPreferences sharedPreferences = getSharedPreferences("bookmarks", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("identity", "");
                                editor.putInt("page", 0);
                                editor.commit();
                                Log.i("FilePath: ", "onLoad: " + pdfUrl);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            try {
                                pdfView.fromFile(pdfUrl)
                                        .onTap(onTapListener)
                                        .onPageScroll(new OnPageScrollListener() {
                                            @Override
                                            public void onPageScrolled(int page, float positionOffset) {
                                                bottomNav.setVisibility(View.VISIBLE);

                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        bottomNav.setVisibility(View.INVISIBLE);
                                                    }
                                                }, 1500);
                                            }
                                        })
                                        .defaultPage(0)
                                        .enableSwipe(true)
                                        .enableAnnotationRendering(true)
                                        .onLoad(BookView.this)
                                        .onPageChange(BookView.this)
                                        .scrollHandle(new DefaultScrollHandle(BookView.this))
                                        .enableDoubletap(true)
                                        .onPageError(BookView.this)
                                        .swipeHorizontal(true)
                                        .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                                        .fitEachPage(true)
                                        .pageSnap(true)
                                        .pageFling(true)
                                        .spacing(0)
                                        .nightMode(nightMode)
                                        .load();

                                Log.i("FilePath: ", "onLoad: " + pdfUrl);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(BookView.this, "" + t.getMessage() + ", File Error", Toast.LENGTH_SHORT).show();

                    }
                });


        sharedPreferences = this.getSharedPreferences("favourites", Context.MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("task_" + id1, false);

        Menu menu = bottomNav.getMenu();
        if (check) {
            menu.findItem(R.id.favourite).setIcon(R.drawable.ic_favourite);
        } else {
            menu.findItem(R.id.favourite).setIcon(R.drawable.ic_favourite_border);
        }


        if (nightMode != true) {
            menu.findItem(R.id.nav_home).setIcon(R.drawable.ic_night);
        } else {
            menu.findItem(R.id.nav_home).setIcon(R.drawable.ic_night_filled);
        }

    }


    @SuppressLint("MissingPermission")
    private void loadBannerAd() {

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void loadComplete(int nbPages) {
        progressDialog.dismiss();
        Log.d("Tag2", "Pages2:" + pdfView.getPageCount());

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        pageNumber = pageNumber;
        totalPage = String.valueOf(pageCount);
        SharedPreferences sharedPreferences = this.getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt("pageNumber_" + id1, 0);
        Boolean clicked = sharedPreferences.getBoolean("pageClicked_" + id1, false);


        int result = value - 1;

        Menu menu = bottomNav.getMenu();
        if (result == pageNumber && clicked) {
            menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark_filled);
        } else {
            menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark_outline);
        }
    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void screenShot() {
        Bitmap b = Bitmap.createBitmap(pdfView.getWidth(), pdfView.getHeight(), Bitmap.Config.ARGB_8888);

        int weight, height;
        weight = pdfView.getWidth();
        height = pdfView.getHeight();
        Bitmap cs = Bitmap.createBitmap(weight, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(cs);
        c.drawBitmap(b, 0, 0, null);
        pdfView.draw(c);
        c.setBitmap(cs);


        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/DCIM/PDFReader");
        dir.mkdir();
        String imagename = time + ".JPEG";
        File file = new File(dir, imagename);
        OutputStream out;
        try {
            out = new FileOutputStream(file);
            cs.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            mp = MediaPlayer.create(this, R.raw.camerashutter);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                }
            });
            //for gallery to be notified of the Image
            MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
            Toast.makeText(BookView.this, "Screenshot Captured", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
                    String[] permission = {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    };
                    requestPermissions(permission, WRITE_EXT_STORAGECODE);
                }
            }
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            int id = item.getItemId();

            switch (id) {

                case R.id.nav_home:
                    SharedPreferences sharedPreferences = getSharedPreferences("bookmarks", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (nightMode != true) {

                        editor.putBoolean("nightMode", true);
                        editor.putString("identity", "itself");
                        editor.putInt("page", pageNumber);
                        editor.commit();
                        item.setIcon(R.drawable.ic_night);
                        recreate();

                        Toast.makeText(BookView.this, "Night Mode turned on", Toast.LENGTH_SHORT).show();
                    } else {
                        editor.putBoolean("nightMode", false);
                        editor.putString("identity", "itself");
                        editor.putInt("page", pageNumber);
                        editor.commit();
                        item.setIcon(R.drawable.ic_night_filled);

                        recreate();
                        Toast.makeText(BookView.this, "Night Mode turned off", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.download:
                    screenShot();
                    break;

                case R.id.bookmark:
                    sharedPreferences = getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
                    int value = sharedPreferences.getInt("pageNumber_" + id1, 0);

                    sharedPreferences = getSharedPreferences("bookmarks", MODE_PRIVATE);
                    editor = sharedPreferences.edit();

                    pageNumber++;
                    //editor.putString("task_" + id1, "true");
                    editor.putBoolean("pageClicked_" + id1, true);
                    editor.putInt("pageNumber_" + id1, pageNumber);
                    editor.putString("pdfUrl_" + id1, url);
                    editor.putString("totalPage_" + id1, totalPage);
                    editor.commit();
                    item.setIcon(R.drawable.ic_bookmark_filled);

                    Log.d(TAG, "Bookmarked_page: " + pageNumber);

                    Toast.makeText(BookView.this, "bookmarked", Toast.LENGTH_SHORT).show();


                    break;

                case R.id.favourite:
                    sharedPreferences = getSharedPreferences("favourites", Context.MODE_PRIVATE);
                    boolean check = sharedPreferences.getBoolean("task_" + id1, false);

                    if (check) {

                        sharedPreferences = getSharedPreferences("favourites", MODE_PRIVATE);
                        editor = sharedPreferences.edit();

                        editor.putBoolean("task_" + id1, false);
                        editor.commit();

                        item.setIcon(R.drawable.ic_favourite_border);
                        Toast.makeText(BookView.this, "Removed from favourites", Toast.LENGTH_SHORT).show();

                    } else {
                        /*saveData(id);*/
                        sharedPreferences = getSharedPreferences("favourites", MODE_PRIVATE);
                        editor = sharedPreferences.edit();

                        editor.putBoolean("task_" + id1, true);
                        editor.commit();

                        item.setIcon(R.drawable.ic_favourite);
                        //item.setIconTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                        Toast.makeText(BookView.this, "Saved in favourites", Toast.LENGTH_SHORT).show();

                    }
                    break;

                case R.id.share:
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Hey, I am reading this. If you want to read it download the app.\n\n*" + bookName + "*\n\n*Read on QSA eBooks:*\nhttps://play.google.com/store/apps/details?id=com.dewmobile.kuaiya.play&hl=en";
                    String shareSubject = "Shareable link";

                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);


                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
                    final Query query = reference.orderByChild("id").equalTo(id1);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot != null) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    Books books = snapshot1.getValue(Books.class);
                                    if (snapshot1.child("shared").exists()) {
                                        String share = books.getShared();
                                        int integerReader = (Integer.parseInt(share)) + 1;
                                        books.setShared(String.valueOf(integerReader));
                                        reference.child(id1).setValue(books);
                                    } else {
                                        books.setShared("1");
                                        reference.child(id1).setValue(books);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                    break;

                default:
                    Toast.makeText(BookView.this, "Nav_Home", Toast.LENGTH_SHORT).show();
                    break;

            }

            return true;
        }
    };

}