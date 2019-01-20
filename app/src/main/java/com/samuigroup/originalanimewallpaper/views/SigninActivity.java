package com.samuigroup.originalanimewallpaper.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.SigninContract;
import com.samuigroup.originalanimewallpaper.presenters.SigninPresenter;

public class SigninActivity extends AppCompatActivity
        implements SigninContract.View {
    // Presenter
    private SigninContract.Presenter presenter;

    private AlertDialog dialog;

    //
    private EditText etxtUsername;
    private EditText etxtPassword;
    private Button btnLogin;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Presenter
        presenter = new SigninPresenter(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setToolbar();
        addControls();
        addEvents();
        buildDialog();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClickSignin(
                        etxtUsername.getText().toString(),
                        etxtPassword.getText().toString());
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.onBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addControls() {
        etxtUsername = findViewById(R.id.etxt_username);
        etxtPassword = findViewById(R.id.etxt_password);
        btnLogin = findViewById(R.id.btn_login);
    }

    private void buildDialog() {
        dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .create();
    }

    // View
    @Override
    public void close() {
        finish();
    }
    @Override
    public void showNotification(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showWaitingDialog() {
        dialog.setMessage("Login..");
        dialog.show();
    }
    @Override
    public void destroyWaitingDialog() {
        dialog.dismiss();
    }

    @Override
    public void switchToSearchImagesView() {
        Intent intent = new Intent(this, SearchImagesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
