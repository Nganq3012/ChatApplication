package com.wruniversity.chatapplication.Youtube;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.wruniversity.chatapplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    VideoAdapter mVideoAdapter;
    List<Video> mVideos=new ArrayList<>();
    RecyclerView mRecyclerViewVideo;
    LinearLayoutManager linearLayoutManager;

    EditText editText;
    Button search;
    AsyncHttpClient client;
    RequestParams params;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_search, container, false);client = new AsyncHttpClient();
        editText = (EditText) v.findViewById(R.id.text_edit);
        search = (Button) v.findViewById(R.id.search_button);
        mRecyclerViewVideo=(RecyclerView)v.findViewById(R.id.rv_video);
        mVideoAdapter=new VideoAdapter(mVideos);
        linearLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerViewVideo.setLayoutManager(linearLayoutManager);
        mRecyclerViewVideo.setAdapter(mVideoAdapter);
        mRecyclerViewVideo.setHasFixedSize(true);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() > 0) {
                    query(editText.getText().toString());
                    editText.setText("");

                }
            }
        });
        mVideoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                //Intent mIntent= new Intent(getActivity(),YoutubeActivity.class);
                Video mVideo=mVideos.get(position);
                //mIntent.putExtra("URL",mVideo.getUrl());
               // mIntent.putExtra("URL2",mVideo.getVideoId());
                //startActivity(mIntent);
            }
        });

        return inflater.inflate(R.layout.fragment_search, container, false);
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
                        mVideoAdapter.notifyDataSetChanged();
                        mVideos.add(video);

                    }


                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(),"Not found video you want",Toast.LENGTH_LONG);
            }


        });


    }
}
