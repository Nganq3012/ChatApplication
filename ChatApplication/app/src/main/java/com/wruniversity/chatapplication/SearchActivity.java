package com.wruniversity.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.wruniversity.chatapplication.Youtube.Config;
import com.wruniversity.chatapplication.Youtube.Video;
import com.wruniversity.chatapplication.Youtube.VideoAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    VideoAdapter mVideoAdapter;
    List<Video> mVideos=new ArrayList<>();
    RecyclerView mRecyclerViewVideo;
    LinearLayoutManager linearLayoutManager;

    EditText editText;
    Button search;
    AsyncHttpClient client;
    RequestParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        client = new AsyncHttpClient();
        editText = (EditText) findViewById(R.id.text_edit);
        search = (Button) findViewById(R.id.search_button);
        mRecyclerViewVideo=(RecyclerView)findViewById(R.id.rv_video);
        mVideoAdapter=new VideoAdapter(mVideos);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        mRecyclerViewVideo.setLayoutManager(linearLayoutManager);
        mRecyclerViewVideo.setAdapter(mVideoAdapter);
        mRecyclerViewVideo.setHasFixedSize(true);
        String s="cat funny";
        query(s);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideos.clear();
                mVideoAdapter.notifyDataSetChanged();
                if (editText.getText().toString().length() > 0) {
                    query(editText.getText().toString() );
                    editText.setText("");

                }
            }
        });
        mVideoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent mIntent= new Intent(SearchActivity.this,YoutubeActivity.class);
                Video mVideo=mVideos.get(position);
                mIntent.putExtra("URL",mVideo.getUrl());
                mIntent.putExtra("URL2",mVideo.getVideoId());
                startActivity(mIntent);
            }
        });

    }

    public void query(String query){
        params = new RequestParams();
        params.put("part","snippet");
        params.put("order", "viewCount");
        params.put("key", Config.SEARCH_API_KEY);
        params.put("maxResults", 30);
        params.put("q", query);
        String url = "https://www.googleapis.com/youtube/v3/search";
        client.get(url, params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject data = new JSONObject(responseString);
                    JSONArray array = data.getJSONArray("items");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        JSONObject id = obj.getJSONObject("id");
                        String videoId = id.getString("videoId");
                        JSONObject snippet = obj.getJSONObject("snippet");
                        String title = snippet.getString("title");
                        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                        JSONObject default1 = thumbnails.getJSONObject("default");
                        String url = default1.getString("url");
                        Video video = new Video(videoId, title, url);
                        mVideos.add(video);
                        mVideoAdapter.notifyDataSetChanged();

                    }


                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(),"Not found video you want",Toast.LENGTH_LONG);
            }


        });


    }

}

