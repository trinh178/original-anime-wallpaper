package com.samuigroup.originalanimewallpaper.views;

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

import com.samuigroup.originalanimewallpaper.ChangePasswordContract;
import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.presenters.ChangePasswordPresenter;

public class ChangePasswordActivity extends AppCompatActivity
implements ChangePasswordContract.View {

    private ChangePasswordContract.Presenter presenter;

    private Toolbar toolbar;
    private EditText etxtPassword;
    private EditText etxtNewPassword;
    private EditText etxtCNewPassword;
    private Button btnOK;

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        presenter = new ChangePasswordPresenter(this);

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
        etxtPassword = findViewById(R.id.etxt_password);
        etxtNewPassword = findViewById(R.id.etxt_newpassword);
        etxtCNewPassword = findViewById(R.id.etxt_cnewpassword);
        btnOK = findViewById(R.id.btn_ok);
    }
    private void addEvents() {
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onChange(etxtPassword.getText().toString(),
                        etxtNewPassword.getText().toString(),
                        etxtCNewPassword.getText().toString());
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
    private void buildDialog() {
        dialog = new AlertDialog.Builder(this)
                .setCancelable(false).create();
    }


    // View
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
    public void clearInput() {
        etxtPassword.setText("");
        etxtNewPassword.setText("");
        etxtCNewPassword.setText("");
    }
    @Override
    public void showError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void close() {
        finish();
    }
}
