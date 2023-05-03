package com.example.beadandoo;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShopActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecycleView;
    private ArrayList<Termekek> mItemList;
    private TermekekAdapter mAdapter;

    private FrameLayout kor;
    private TextView contentTextView;
    private int gridNumber = 1;
    private int carItems = 0;
    private boolean viewRow = true;

    private NotificationHandler mNotificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "bejelentkezet felhasznalo");
        } else {
            Log.d(LOG_TAG, "nem bejelentkezet felhasznalo");
            finish();
        }

        mRecycleView = findViewById(R.id.recycleView);
        mRecycleView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();
        mAdapter = new TermekekAdapter(this, mItemList);
        mRecycleView.setAdapter(mAdapter);

        intializeData();

        mNotificationHandler = new NotificationHandler(this);

        mNotificationHandler.send("Sikeres bejelentkezés! :)");
    }

    private TextView mTitleText;
    private TextView mInfotext;
    private TextView mArText;
    private ImageView TermekKep;
    private RatingBar csillagok;

    private void intializeData() {
        String[] itemList = getResources().getStringArray(R.array.termekek_nevei);
        String[] itemsInfo = getResources().getStringArray(R.array.leiras);
        String[] itemAr = getResources().getStringArray(R.array.ar);

        TypedArray itemscsillag = getResources().obtainTypedArray(R.array.csillagok);
        TypedArray itemsImagineResource = getResources().obtainTypedArray(R.array.kepek);
        mItemList.clear();

        for (int i = 0; i < itemList.length; i++) {
            mItemList.add(new Termekek(itemList[i], itemsInfo[i], itemAr[i], itemscsillag.getFloat(i, 0), itemsImagineResource.getResourceId(i, 0)));
        }
        itemsImagineResource.recycle();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_button:
                Log.d(LOG_TAG, "log out clicked");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.cart:
                Log.d(LOG_TAG, "kocsihozadas clicked");
                return true;
            case R.id.setting_button:
                Log.d(LOG_TAG, "setting butten clicked");
                return true;
            case R.id.view_selector:
                Log.d(LOG_TAG, "view selector clicked");
                if (viewRow) {
                    changeSpanCount(item, R.drawable.ic_view_grid, 1);
                } else {
                    changeSpanCount(item, R.drawable.ic_view_row, 2);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) mRecycleView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();
        kor = (FrameLayout) rootView.findViewById(R.id.kor);
        contentTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon() {
        carItems = (carItems + 1);
        if (0 < carItems) {
            contentTextView.setText(String.valueOf(carItems));
        } else {
            contentTextView.setText("");
        }
    }
}