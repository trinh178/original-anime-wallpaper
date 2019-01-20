package com.samuigroup.originalanimewallpaper.models.socialnetwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.samuigroup.originalanimewallpaper.R;
import com.samuigroup.originalanimewallpaper.models.entities.Category;
import com.samuigroup.originalanimewallpaper.models.entities.Comment;
import com.samuigroup.originalanimewallpaper.models.entities.User;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnChangeMyAvatarListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnChangeMyPasswordListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnCommentListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnFavoriteListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadAvatarListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadCategoriesListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadCommentsListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadFavoritesListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnLoadUserListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnSigninListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnSignupListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnUnfavoriteListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnUpdateCommentsListener;
import com.samuigroup.originalanimewallpaper.models.socialnetwork.listeners.OnUpdateFavoritesListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class FirebaseService implements SocialNetworkAPI {
    // Singleton
    private static FirebaseService instance = null;
    public static synchronized FirebaseService getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseService(context);
        }
        return instance;
    }

    //
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    private User myUser;
    // Cache
    private File userFile;
    private Bitmap defaultAvatarBmp;

    // Context
    private Context context;

    public FirebaseService(Context context) {
        this.context = context;

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Cache
        userFile = new File(context.getCacheDir().getPath() + "/myuser");
        defaultAvatarBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_avatar);
    }

    // API

    // Authentication
    @Override
    public void signup(final String username, String password, final OnSignupListener listener) {
        auth.createUserWithEmailAndPassword(username + "@samui.com", password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("favorites", new ArrayList<>());
                            firestore.collection("users").document(username).set(data)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                listener.onSuccess();
                                            } else {
                                                listener.onFailure(task.getException().getMessage());
                                            }
                                        }
                                    });
                        } else {
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }
    @Override
    public void signin(final String username, String password, final OnSigninListener listener) {
        auth.signInWithEmailAndPassword(username + "@samui.com", password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadUser(username, new OnLoadUserListener() {
                                @Override
                                public void onSuccess(User user) {
                                    myUser = user;
                                    // cache
                                    try {
                                        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(userFile));
                                        out.writeObject(myUser);
                                        out.close();
                                        listener.onSuccess(user);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        listener.onFailure(e.getMessage());
                                    }
                                }
                                @Override
                                public void onFailure(String msg) {
                                    listener.onFailure(msg);
                                }
                            });
                        } else {
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }
    @Override
    public void signout() {
        auth.signOut();
        userFile.delete();
        myUser = null;
    }
    @Override
    public boolean isSignin() {
        if (myUser != null) return true;
        if (!userFile.exists()) return false;
        try {
            ObjectInput in = new ObjectInputStream(new FileInputStream(userFile));
            myUser = (User) in.readObject();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public User getMyUser() {
        return myUser;
    }

    // User
    @Override
    public void changeMyAvatar(Bitmap bmp, final OnChangeMyAvatarListener listener) {
        if (isSignin()) {
            bmp = Bitmap.createScaledBitmap(bmp, 200, 200, false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            storage.getReference("avatars/" + myUser.getUsername()).putBytes(data)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                File file = new File(context.getFilesDir().getPath() + "/avatar_" + myUser.getUsername());
                                file.delete();
                                listener.onSuccess();
                            } else {
                                listener.onFailure(task.getException().getMessage());
                            }
                        }
                    });
        } else {
            listener.onFailure("No login");
        }
    }
    @Override
    public void changeMyPassword(String oldPassword, final String newPassword, final OnChangeMyPasswordListener listener) {
        final FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            listener.onFailure("No login");
            return;
        }
        user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), oldPassword))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                listener.onSuccess();
                                            } else {
                                                listener.onFailure(task.getException().getMessage());
                                            }
                                        }
                                    });
                        } else {
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }
    @Override
    public void loadAvatar(String username, final OnLoadAvatarListener listener) {
        final File file = new File(context.getFilesDir().getPath() + "/avatar_" + username);
        try {
            if (file.exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(file.getPath());
                if (bmp == null)
                    bmp = defaultAvatarBmp;
                listener.onSuccess(bmp);
            } else {
                file.createNewFile();
                storage.getReference("avatars/" + username).getFile(file)
                        .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Bitmap bmp = BitmapFactory.decodeFile(file.getPath());
                                    if (bmp == null)
                                        bmp = defaultAvatarBmp;
                                    listener.onSuccess(bmp);
                                } else {
                                    listener.onFailure(task.getException().getMessage());
                                }
                            }
                        });
            }
        } catch (IOException e) {
            listener.onFailure(e.getMessage());
        }
    }
    @Override
    public void loadUser(final String username, final OnLoadUserListener listener) {
        firestore.collection("users").document(username).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            User user = new User(
                                    username,
                                    (ArrayList<String>) task.getResult().get("favorites")
                            );
                            listener.onSuccess(user);
                        } else {
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }

    // Favorite
    @Override
    public void favorite(final String postId, final OnFavoriteListener listener) {
        if (!isSignin()) {
            listener.onFailure("No login");
            return;
        }
        firestore.collection("users").document(myUser.getUsername())
                .update("favorites", FieldValue.arrayUnion(postId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firestore.collection("posts").document(postId).collection("favorites")
                                    .document(myUser.getUsername())
                                    .set(new HashMap<>())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                myUser.getFavorites().add(postId);
                                                listener.onSuccess();
                                            } else {
                                                listener.onFailure(task.getException().getMessage());
                                            }
                                        }
                                    });
                        } else {
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }
    @Override
    public void unfavorite(final String postId, final OnUnfavoriteListener listener) {
        if (!isSignin()) {
            listener.onFailure("No login");
            return;
        }
        firestore.collection("users").document(myUser.getUsername())
                .update("favorites", FieldValue.arrayRemove(postId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firestore.collection("posts").document(postId).collection("favorites")
                                    .document(myUser.getUsername())
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                myUser.getFavorites().remove(postId);
                                                listener.onSuccess();
                                            } else {
                                                listener.onFailure(task.getException().getMessage());
                                            }
                                        }
                                    });
                        } else {
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }
    @Override
    public void loadFavorites(String postId, final OnLoadFavoritesListener listener) {
        firestore.collection("posts").document(postId).collection("favorites")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String[] users = new String[task.getResult().size()];
                            int i = 0;
                            for (DocumentSnapshot doc: task.getResult().getDocuments()) {
                                users[i] = doc.getId();
                                i ++;
                            }
                            listener.onSuccess(users);
                        } else {
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });

        firestore.collection("posts").document(postId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().contains("favorites"))
                                listener.onSuccess((String[]) task.getResult().get("favorites"));
                            else
                                listener.onSuccess(new String[0]);
                        } else {
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }
    @Override
    public Listener addOnUpdateFavoritesListener(String postId, final OnUpdateFavoritesListener listener) {
        final ListenerRegistration l = firestore.collection("posts").document(postId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            listener.onChanged();
                        }
                    }
                });
        return new Listener() {
            @Override
            public void remove() {
                l.remove();
            }
        };
    }

    // Comment
    @Override
    public void comment(final String postId, String text, final OnCommentListener listener) {
        if (!isSignin()) {
            listener.onFailure("No login");
            return;
        }

        //
        Map<String, Object> data = new HashMap<>();
        data.put("username", myUser.getUsername());
        data.put("date", FieldValue.serverTimestamp());
        data.put("text", text);

        //
        firestore.collection("posts").document(postId).collection("comments").add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            task.getResult().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Comment comment = new Comment(
                                                task.getResult().getId(),
                                                postId,
                                                task.getResult().getString("username"),
                                                task.getResult().getDate("date"),
                                                task.getResult().getString("text")
                                        );
                                        listener.onSuccess(comment);
                                    } else {
                                        listener.onFailure(task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }
    @Override
    public void loadComments(final String postId, final OnLoadCommentsListener listener) {
        firestore.collection("posts").document(postId).collection("comments").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Comment[] comments = new Comment[task.getResult().getDocuments().size()];
                            Comment comment;
                            int i = 0;
                            for (DocumentSnapshot doc: task.getResult().getDocuments()) {
                                comment = new Comment(
                                        doc.getId(),
                                        postId,
                                        doc.getString("username"),
                                        doc.getDate("date"),
                                        doc.getString("text")
                                );
                                comments[i] = comment;
                                i ++;
                            }
                            listener.onSuccess(comments);
                        } else {
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }
    @Override
    public Listener addOnUpdateCommentsListener(final String postId, final OnUpdateCommentsListener listener) {
        final ListenerRegistration l = firestore.collection("posts").document(postId)
                .collection("comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            if (queryDocumentSnapshots != null) {
                                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
                                    switch (doc.getType()) {
                                        case ADDED:
                                            Comment comment = new Comment(
                                                    doc.getDocument().getId(),
                                                    postId,
                                                    doc.getDocument().getString("username"),
                                                    doc.getDocument().getDate("date"),
                                                    doc.getDocument().getString("text")
                                            );
                                            listener.onAdded(comment);
                                    }
                                }
                            }
                        }
                    }
                });
        return new Listener() {
            @Override
            public void remove() {
                l.remove();
            }
        };
    }

    // Other
    @Override
    public void loadCategories(final OnLoadCategoriesListener listener) {
        firestore.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Category[] categories = new Category[task.getResult().getDocuments().size()];
                            Category category;
                            int i = 0;
                            for (DocumentSnapshot doc: task.getResult().getDocuments()) {
                                category = new Category(
                                        doc.getString("name"),
                                        doc.getString("tag"),
                                        doc.getString("icon_url"),
                                        doc.getString("description")
                                );
                                categories[i] = category;
                                i ++;
                            }
                            listener.onSuccess(categories);
                        } else {
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }
}
