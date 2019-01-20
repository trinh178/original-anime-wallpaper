package com.samuigroup.originalanimewallpaper.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.SignupContract;
import com.samuigroup.originalanimewallpaper.presenters.SignupPresenter;

public class SignupActivity extends AppCompatActivity
implements SignupContract.View {

    private SignupContract.Presenter presenter;

    private Toolbar toolbar;
    private EditText etxtUsername;
    private EditText etxtPassword;
    private EditText etxtCPassword;
    private Button btnSignup;

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        presenter = new SignupPresenter(this);

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
    private void addControls() {
        etxtUsername = findViewById(R.id.etxt_username);
        etxtPassword = findViewById(R.id.etxt_password);
        etxtCPassword = findViewById(R.id.etxt_cpassword);
        btnSignup = findViewById(R.id.btn_signup);
    }
    private void addEvents() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSignup(etxtUsername.getText().toString(),
                        etxtPassword.getText().toString(),
                        etxtCPassword.getText().toString());
            }
        });
    }
    private void buildDialog() {
        dialog = new AlertDialog.Builder(this)
                .setCancelable(false).create();
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

    // View
    @Override
    public void close() {
        finish();
    }
    @Override
    public void showError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showDialog(String text) {
        dialog.setMessage(text);
        dialog.show();
    }
    @Override
    public void closeDialog() {
        dialog.dismiss();
    }
    @Override
    public void opemSigninView() {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
    }
}
