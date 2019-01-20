package com.samuigroup.originalanimewallpaper.presenters;

import android.content.Context;
import android.graphics.Bitmap;

import com.samuigroup.originalanimewallpaper.PostContract;
import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.models.Model;
import com.samuigroup.originalanimewallpaper.models.entities.Comment;
import com.samuigroup.originalanimewallpaper.models.entities.Post;
import com.samuigroup.originalanimewallpaper.models.entities.User;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.SocialNetworkAPI;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnCommentListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnFavoriteListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadAvatarListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadCommentsListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadFavoritesListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnUnfavoriteListener;

import java.util.ArrayList;

public class PostPresenter implements PostContract.Presenter {
    private PostContract.View view;
    private Post post;
    private ArrayList<String> favoriteUsers;

    public PostPresenter(PostContract.View view) {
        this.view = view;
    }

    @Override
    public void onStart(Post post) {
        this.post = post;
        // load comment
        Model.getSocialNetworkAPI((Context) view).loadComments(post.getId(), new OnLoadCommentsListener() {
            @Override
            public void onSuccess(Comment[] comments) {
                for (final Comment comment: comments) {
                    Model.getSocialNetworkAPI((Context) view).loadAvatar(comment.getUsername(), new OnLoadAvatarListener() {
                        @Override
                        public void onSuccess(Bitmap bmp) {
                            view.showAdditionalComment(comment, bmp);
                        }
                        @Override
                        public void onFailure(String msg) {
                            view.showError(msg);
                        }
                    });
                }
            }
            @Override
            public void onFailure(String msg) {
                view.showError(msg);
            }
        });
        // load favorite
        favoriteUsers = new ArrayList<>();
        Model.getSocialNetworkAPI((Context) view).loadFavorites(post.getId(), new OnLoadFavoritesListener() {
            @Override
            public void onSuccess(String[] users) {
                for (String user: users) {
                    favoriteUsers.add(user);
                }
                view.showFavoriteCount(favoriteUsers.size());
                if (Model.getSocialNetworkAPI((Context) view).isSignin()) {
                    if (favoriteUsers.contains(Model.getSocialNetworkAPI((Context) view).getMyUser().getUsername())) {
                        view.showFavoriteIcon(1);
                    } else {
                        view.showFavoriteIcon(0);
                    }
                }
            }
            @Override
            public void onFailure(String msg) {
                view.showFavoriteCount(favoriteUsers.size());
                view.showError(msg);
            }
        });
    }
    @Override
    public void onBack() {
        view.close();
    }
    @Override
    public void onTagClick(String tag) {
        view.openSearchImagesView(new String[] {tag});
        view.close();
    }
    @Override
    public void onComment(String text) {
        if (text.length() < 10) {
            view.showError(((Context) view).getResources().getString(R.string.comment_too_short));
            return;
        }
        view.showDialog("comment..");
        Model.getSocialNetworkAPI((Context) view).comment(post.getId(), text, new OnCommentListener() {
            @Override
            public void onSuccess(final Comment comment) {
                Model.getSocialNetworkAPI((Context) view).loadAvatar(comment.getUsername(), new OnLoadAvatarListener() {
                    @Override
                    public void onSuccess(Bitmap bmp) {
                        view.showAdditionalComment(comment, bmp);
                        view.clearInput();
                        view.closeDialog();
                        view.focusLatestComment();
                    }
                    @Override
                    public void onFailure(String msg) {
                        view.closeDialog();
                        view.showError(msg);
                    }
                });
            }
            @Override
            public void onFailure(String msg) {
                view.closeDialog();
                view.showError(msg);
            }
        });
    }
    @Override
    public void onFavoriteClick() {
        SocialNetworkAPI api = Model.getSocialNetworkAPI((Context) view);
        if (api.isSignin()) {
            final User myuser = api.getMyUser();
            view.showFavoriteIcon(2);
            if (favoriteUsers.contains(myuser.getUsername())) {
                api.unfavorite(post.getId(), new OnUnfavoriteListener() {
                    @Override
                    public void onSuccess() {
                        favoriteUsers.remove(myuser.getUsername());
                        view.showFavoriteCount(favoriteUsers.size());
                        view.showFavoriteIcon(0);
                    }
                    @Override
                    public void onFailure(String msg) {
                        view.showError(msg);
                    }
                });
            } else {
                api.favorite(post.getId(), new OnFavoriteListener() {
                    @Override
                    public void onSuccess() {
                        favoriteUsers.add(myuser.getUsername());
                        view.showFavoriteCount(favoriteUsers.size());
                        view.showFavoriteIcon(1);
                    }
                    @Override
                    public void onFailure(String msg) {
                        view.showError(msg);
                    }
                });
            }
        } else {
            view.showError("No login");
        }
    }
    @Override
    public void onZoom() {
        view.showZoom(true);
    }
}
