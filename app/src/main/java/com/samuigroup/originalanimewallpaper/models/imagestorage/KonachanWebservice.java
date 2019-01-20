package com.samuigroup.originalanimewallpaper.models.imagestorage;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.samuigroup.originalanimewallpaper.models.entities.Post;
import com.samuigroup.originalanimewallpaper.models.imagestorage.listeners.OnLoadPostListener;
import com.samuigroup.originalanimewallpaper.models.imagestorage.listeners.OnLoadPostsListener;
import com.samuigroup.originalanimewallpaper.models.imagestorage.listeners.OnSearchPostsListener;
import com.samuigroup.originalanimewallpaper.models.imagestorage.listeners.OnSearchTagsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class KonachanWebservice implements ImageStorageAPI {
    // Singleton
    private static KonachanWebservice instance = null;
    public static synchronized KonachanWebservice getInstance(Context context) {
        if (instance == null) {
            instance = new KonachanWebservice(context);
        }
        return instance;
    }

    private final String SERVER = "https://konachan.com";

    //Volley
    private RequestQueue requestQueue;

    public KonachanWebservice(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    // API
    // Post - Image
    @Override
    public void loadPost(String postId, final OnLoadPostListener listener) {
        String url = SERVER + "/post.json?tags=id:" + postId;

        Response.Listener<String> successListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 1) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Post post = new Post(
                                jsonObject.getString("id"),
                                jsonObject.getString("author"),
                                jsonObject.getString("preview_url"),
                                jsonObject.getString("sample_url"),
                                jsonObject.getString("file_url"),
                                jsonObject.getString("tags").split(" ")
                        );
                        listener.onSuccessful(post);
                    } else {
                        listener.onFailure("Not found!");
                    }
                } catch (JSONException e) {
                    listener.onFailure(e.getMessage());
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFailure(error.getMessage());
            }
        };

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                successListener,
                errorListener
        );

        requestQueue.add(request);
    }
    @Override
    public void loadPosts(String[] postIds, OnLoadPostsListener listener) {
        String[] postIdsLoad = postIds.clone();
        Post[] posts = new Post[postIds.length];

        recursiveLoadPost(postIdsLoad, posts, 0, listener);
    }
    private void recursiveLoadPost(final String[] postIdsLoad, final Post[] posts, final int position, final OnLoadPostsListener listener) {
        if (position < postIdsLoad.length) {
            loadPost(postIdsLoad[position], new OnLoadPostListener() {
                @Override
                public void onSuccessful(Post post) {
                    posts[position] = post;
                    recursiveLoadPost(postIdsLoad, posts, position + 1, listener);
                }
                @Override
                public void onFailure(String message) {
                    listener.onFailure(message);
                }
            });
        } else {
            listener.onSuccessful(posts);
        }
    }
    @Override
    public void searchPosts(String[] hashtags, final int limit, final int page, final OnSearchPostsListener listener) {
        String url = SERVER + "/post.xml?limit="+limit+"&page="+page+"&tags=rating:safe+";
        for (String hashtag: hashtags) {
            url += "+" + hashtag;
        }
        Response.Listener<String> successListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    Post post;
                    Post[] posts = new Post[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        post = new Post(
                                jsonObject.getString("author"),
                                jsonObject.getString("preview_url"),
                                jsonObject.getString("sample_url"),
                                jsonObject.getString("file_url")
                        );
                        posts[i] = post;
                    }

                    listener.onSuccessful(posts, limit, page, );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(new InputSource(new StringReader(response)));

                    NodeList nodeList = document.getElementsByTagName("post");
                    Element element;
                    Post post;
                    Post[] posts = new Post[nodeList.getLength()];
                    for (int i = 0; i < nodeList.getLength(); i ++) {
                        element = (Element) nodeList.item(i);
                        post = new Post(
                                element.getAttribute("id"),
                                element.getAttribute("author"),
                                element.getAttribute("preview_url"),
                                element.getAttribute("sample_url"),
                                element.getAttribute("file_url"),
                                element.getAttribute("tags").split(" ")
                        );
                        posts[i] = post;
                    }
                    listener.onSuccessful(posts, limit, page, Integer.parseInt(document.getDocumentElement().getAttribute("count")));
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFailure(error.getMessage());
            }
        };
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                successListener,
                errorListener
        );
        requestQueue.add(request);
    }
    // Tag
    @Override
    public void searchTags(String name, int limit, final OnSearchTagsListener listener) {
        String url = SERVER + "/tag.json?order=count&limit="+limit+"&name="+name;
        Response.Listener<String> successListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    String[] tags = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        tags[i] = jsonObject.getString("name");
                    }
                    listener.onSuccessful(tags);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //listener.onFailure("");
            }
        };
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                successListener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(request);
    }
}
