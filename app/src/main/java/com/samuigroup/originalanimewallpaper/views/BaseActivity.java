package com.samuigroup.originalanimewallpaper.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.samuigroup.originalanimewallpaper.BaseContract;
import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.views.adapters.TagsMACTVAdapter;
import com.startapp.android.publish.adsCommon.StartAppAd;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class BaseActivity extends AppCompatActivity
implements BaseContract.View {

    // Toolbar
    private Toolbar toolbar;
    // Search bar
    private MultiAutoCompleteTextView mactxtSearch;
    private TagsMACTVAdapter searchAdapter;
    private ImageButton btnSearch;
    // Drawer
    private DrawerLayout drawerLayout;
    // Navigation
    private NavigationView navigationView;
    private CircleImageView civAvatar;
    private TextView txtUsername;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        View view = getLayoutInflater().inflate(layoutResID, null, false);
        setDefaultContentView(view);
    }
    @Override
    public void setContentView(View view) {
        View baseView = getLayoutInflater().inflate(R.layout.activity_base, null, false);
        super.setContentView(baseView);
        setDefaultContentView(view);
    }
    private void setDefaultContentView(View view) {
        ViewGroup container = findViewById(R.id.container);
        container.addView(view);

        // Toolbar
        setToolbar();
        // Drawer
        setDrawer();
        // Events
        addEvents();

        //
        getBasePresenter().onStart();

        // Hide keyboard
        View v = getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            v.clearFocus();
        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.icon);
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    private void setDrawer() {
        drawerLayout = findViewById(R.id.drawerLayout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Drawer button
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEvents() {
        // Search bar
        mactxtSearch = findViewById(R.id.mactxt_search);
        mactxtSearch.setThreshold(1);
        mactxtSearch.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        searchAdapter = new TagsMACTVAdapter(this, getBasePresenter());
        mactxtSearch.setAdapter(searchAdapter);

        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mactxtSearch.getText().toString();

                // Hide keyboard
                View vv = getCurrentFocus();
                if (vv != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(vv.getWindowToken(), 0);
                    vv.clearFocus();
                }

                onSearchClickListener(text.split(", "));
            }
        });

        // Navigation
        navigationView = findViewById(R.id.navigationView);
        civAvatar = navigationView.getHeaderView(0).findViewById(R.id.civ_avatar);
        txtUsername = navigationView.getHeaderView(0).findViewById(R.id.txt_username);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_changeavatar:
                        getBasePresenter().onNavClickChangeAvatar();
                        break;
                    case R.id.nav_changepassword:
                        getBasePresenter().onNavClickChangePassword();
                        break;
                    case R.id.nav_categories:
                        getBasePresenter().onNavClickCategories();
                        break;
                    case R.id.nav_imagesfavorite:
                        getBasePresenter().onNavClickImagesFavorite();
                        break;
                    case R.id.nav_signout:
                        getBasePresenter().onNavClickSignout();
                        break;
                    case R.id.nav_exit:
                        getBasePresenter().onNavClickExit();
                        break;
                    case R.id.nav_signup:
                        getBasePresenter().onNavClickSignup();
                        break;
                    case R.id.nav_signin:
                        getBasePresenter().onNavClickSignin();
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    // Base API
    protected void onSearchClickListener(String[] tags) {

    }

    //
    @Override
    protected void onResume() {
        super.onResume();
        getBasePresenter().onStart();
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawers();
        } else {
            drawerLayout.openDrawer(Gravity.START);
        }
    }

    // Presenter
    protected abstract BaseContract.Presenter getBasePresenter();

    // View
    @Override
    public void showTagsSuggestion(String[] tags) {
        searchAdapter.clear();
        for (String s: tags) {
            searchAdapter.add(s);
        }
        searchAdapter.notifyDataSetChanged();
    }
    @Override
    public void showAvatar(Bitmap bmp) {
        civAvatar.setImageBitmap(bmp);
    }
    @Override
    public void showUsername(String username) {
        txtUsername.setText(username);
    }
    @Override
    public void setTagsText(String tags) {
        mactxtSearch.setText(tags);
    }
    @Override
    public void close() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage(R.string.exit_confirm)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StartAppAd.onBackPressed(BaseActivity.this);
                                finish();
                            }
                        })
                .setNegativeButton("NO", null)
                .create();
        dialog.show();
    }
    @Override
    public void setNavigationMenu(boolean isSignin) {
        navigationView.getMenu().clear();
        if (isSignin) {
            navigationView.getHeaderView(0).setVisibility(View.VISIBLE);
            navigationView.inflateMenu(R.menu.navigation_menu_signin);
        } else {
            navigationView.getHeaderView(0).setVisibility(View.INVISIBLE);
            navigationView.inflateMenu(R.menu.navigation_menu);
        }
    }
    @Override
    public void openChangeAvatarView() {
        Intent intent = new Intent(this, ChangeAvatarActivity.class);
        startActivity(intent);
    }
    @Override
    public void openChangePasswordView() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }
    @Override
    public void openMyFavoritesView() {
        Intent intent = new Intent(this, MyFavoritesActivity.class);
        startActivity(intent);
    }
    @Override
    public void openSigninView() {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
    }
    @Override
    public void openSignupView() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    @Override
    public void openCategories() {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }
}
