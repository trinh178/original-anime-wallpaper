package com.samuigroup.originalanimewallpaper.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ortiz.touchview.TouchImageView;
import com.samuigroup.originalanimewallpaper.PostContract;
import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.models.entities.Comment;
import com.samuigroup.originalanimewallpaper.models.entities.Post;
import com.samuigroup.originalanimewallpaper.presenters.PostPresenter;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.VideoListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

public class PostActivity extends AppCompatActivity
implements PostContract.View{
    // Presenter
    private PostContract.Presenter presenter;

    private Post post;

    //
    private Toolbar toolbar;
    private ScrollView scrollView;
    private ImageView ivImage;
    private TextView txtAuthor;
    private ViewGroup tagsContainer;
    private ProgressBar progressBar;
    private ViewGroup commentContainer;
    private ImageButton btnComment;
    private EditText etxtComment;
    private ImageView ivFavorite;
    private TextView txtFavoriteCount;
    private ImageView ivZoom;
    private ImageView ivDownload;
    private Dialog imageZoomDialog;
    private AlertDialog dialog;
    private AlertDialog downloadDialog;

    // Ads
    private StartAppAd startAppAd = new StartAppAd(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Presenter
        presenter = new PostPresenter(this);

        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //
        post = (Post) getIntent().getSerializableExtra("post");

        //
        addControl();
        addEvents();
        setToolbar();
        buildDialog();
        //buildImageZoomDialog();
        load();

        //
        setTitle(R.string.post);
        //
        presenter.onStart(post);


        //
        addBannerAd();

    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addControl() {
        scrollView = findViewById(R.id.scrollview);
        ivImage = findViewById(R.id.iv_image);
        txtAuthor = findViewById(R.id.txt_author);
        tagsContainer = findViewById(R.id.tags_container);
        progressBar = findViewById(R.id.loading);
        commentContainer = findViewById(R.id.comment_containter);
        btnComment = findViewById(R.id.btn_comment);
        etxtComment = findViewById(R.id.etxt_comment);
        ivFavorite = findViewById(R.id.iv_favorite);
        txtFavoriteCount = findViewById(R.id.txt_favorite_count);
        ivZoom = findViewById(R.id.iv_zoom);
        ivDownload = findViewById(R.id.iv_download);
    }
    private void addEvents() {
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide keyboard
                View vv = getCurrentFocus();
                if (vv != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(vv.getWindowToken(), 0);
                    vv.clearFocus();
                }

                presenter.onComment(etxtComment.getText().toString());
            }
        });
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onFavoriteClick();
            }
        });
        ivZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onZoom();
            }
        });
        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadDialog.show();
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

    private void load() {
        // Image
        Glide
                .with(this)
                .load(post.getSampleUrl())
                .apply(new RequestOptions().placeholder(R.drawable.bg_loading))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        ((ViewGroup)progressBar.getParent()).removeView(progressBar);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ((ViewGroup)progressBar.getParent()).removeView(progressBar);
                        buildImageZoomDialog(resource);
                        return false;
                    }
                })
                .into(ivImage);
        // Author
        txtAuthor.setText(post.getAuthor());
        // Tags
        for (String tag: post.getTags()) {
            addTagView(tag);
        }
    }

    private void buildImageZoomDialog(Drawable drawable) {

        imageZoomDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        imageZoomDialog.setContentView(R.layout.dialog_imagezoom);
        TouchImageView tivImage = imageZoomDialog.findViewById(R.id.tiv_image);
        tivImage.setImageDrawable(drawable);
    }

    // Tags
    private void addTagView(final String tag) {
        Button btn = (Button) getLayoutInflater().inflate(R.layout.tag, null, false);
        btn.setText(tag);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onTagClick(tag);
            }
        });
        tagsContainer.addView(btn);
    }
    private void addCommentView(Comment comment, Bitmap avatar) {
        View view = getLayoutInflater().inflate(R.layout.comment, null, false);
        ImageView ivAvatar = view.findViewById(R.id.iv_avatar);
        TextView txtDate = view.findViewById(R.id.txt_date);
        TextView txtText = view.findViewById(R.id.txt_text);
        TextView txtCommenter = view.findViewById(R.id.txt_commenter);
        ivAvatar.setImageBitmap(avatar);
        txtDate.setText(comment.getDate().toString());
        txtText.setText(comment.getText());
        txtCommenter.setText(comment.getUsername());
        commentContainer.addView(view);
    }
    private void buildDialog() {
        dialog = new AlertDialog.Builder(this)
                .setCancelable(false).create();

        downloadDialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage(R.string.download_confirm)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDialog("get link ..");
                        startAppAd.setVideoListener(new VideoListener() {
                            @Override
                            public void onVideoCompleted() {
                                DownloadManager.Request r = new DownloadManager.Request(Uri.parse(post.getFileUrl()));
                                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                String fname = URLUtil.guessFileName(post.getFileUrl(), null, null).toLowerCase();
                                fname = fname.replace("konachan.com", "animewallpaper");
                                r.setTitle(fname);
                                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                dm.enqueue(r);
                                Toast.makeText(PostActivity.this, R.string.get_link_completed, Toast.LENGTH_LONG).show();
                            }
                        });
                        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                            @Override
                            public void onReceiveAd(Ad ad) {
                                closeDialog();
                                startAppAd.showAd();
                            }

                            @Override
                            public void onFailedToReceiveAd(Ad ad) {
                                closeDialog();
                                showError("failed!");
                            }
                        });
                    }
                })
                .setNegativeButton("NO", null)
                .create();
    }

    private void addBannerAd() {
        Banner adBanner = findViewById(R.id.banner);
        adBanner.loadAd();
    }

    // View
    @Override
    public void openSearchImagesView(String[] tags) {
        Intent intent = new Intent(this, SearchImagesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tags", tags);
        startActivity(intent);
    }
    @Override
    public void showAdditionalComment(Comment comment, Bitmap avatar) {
        addCommentView(comment, avatar);
    }
    @Override
    public void showFavoriteIcon(int favoriteStatus) {
        switch (favoriteStatus) {
            case 0:
                ivFavorite.setImageResource(R.drawable.icon_heart_border);
                break;
            case 1:
                ivFavorite.setImageResource(R.drawable.icon_heart);
                break;
            case 2:
                ivFavorite.setImageResource(R.drawable.icon_loading);
                break;
        }
    }
    @Override
    public void showFavoriteCount(int count) {
        txtFavoriteCount.setText(String.valueOf(count));
    }
    @Override
    public void showZoom(boolean show) {
        if (show)
            imageZoomDialog.show();
        else
            imageZoomDialog.dismiss();
    }
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
    public void clearInput() {
        etxtComment.setText("");
    }
    @Override
    public void focusLatestComment() {
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
