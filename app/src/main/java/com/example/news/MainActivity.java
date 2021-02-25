package com.example.news;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NewsItemClicked{
    private RecyclerView recyclerView;
    ArrayList<String> strings;
    NewsListAdapter newsListAdapter;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));



        fetch_data();
        try {
            getHeaders();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }


        newsListAdapter=new NewsListAdapter(this);
        recyclerView.setAdapter(newsListAdapter);
    }

    private void fetch_data() {
        String url = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json";
        final JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<NewsData> newsDatalist = new ArrayList<>();
                        try {
                            JSONArray jsonArrayNews = response.getJSONArray("articles");

                            for (int i = 0; i < jsonArrayNews.length(); i++) {
                                JSONObject json = jsonArrayNews.getJSONObject(i);
                                NewsData newsData = new NewsData(json.getString("title"), json.getString("author"),
                                        json.getString("url"), json.getString("urlToImage"));
                                newsDatalist.add(newsData);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        newsListAdapter.updateNews(newsDatalist);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObject);

    }
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    @Override
    public void OnItemClicked(NewsData item) {

        String url = item.url;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
        //int colorInt = Color.parseColor("#FFFFFF"); //red
        //builder.setToolbarColor(colorInt);

    }

}