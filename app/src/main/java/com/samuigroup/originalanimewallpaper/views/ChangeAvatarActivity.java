package com.samuigroup.originalanimewallpaper.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.samuigroup.originalanimewallpaper.ChangeAvatarContract;
import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.presenters.ChangeAvatarPresenter;

import java.io.IOException;

public class ChangeAvatarActivity extends AppCompatActivity
implements ChangeAvatarContract.View {

    private ChangeAvatarContract.Presenter presenter;

    private Toolbar toolbar;
    private ImageView ivAvatar;
    private Button btnChange;

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);

        presenter = new ChangeAvatarPresenter(this);

        setToolbar();
        addControls();
        addEvents();
        buildDialog();

        presenter.onStart();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    private void addControls() {
        ivAvatar = findViewById(R.id.iv_avatar);
        btnChange = findViewById(R.id.btn_change);
    }
    private void addEvents() {
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 178);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 178) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    presenter.onChange(bmp);
                } catch (IOException e) {
                    showError(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    // View
    @Override
    public void close() {
        finish();
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
    public void showError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showAvatar(Bitmap bmp) {
        ivAvatar.setImageBitmap(bmp);
    }
}
