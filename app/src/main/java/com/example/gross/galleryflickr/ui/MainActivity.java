package com.example.gross.galleryflickr.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.gross.galleryflickr.Adapter;
import com.example.gross.galleryflickr.Constants;
import com.example.gross.galleryflickr.R;
import com.example.gross.galleryflickr.controller.RestManager;
import com.example.gross.galleryflickr.model.Info;
import com.example.gross.galleryflickr.model.Photo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private List<Photo> mListPhoto;
    private GridView gridView;
    private RestManager mManeger;
    private Map<String, String> parameters = new HashMap<>();
    private String searchText = "";
    private boolean isRestart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        parameters.put("method",Constants.METHOD_INTERESTINGNESS);
        parameters.put("api_key",Constants.API_KEY);
        parameters.put("format",Constants.FORMAT);
        parameters.put("per_page",Constants.PER_PAGE);
        parameters.put("nojsoncallback",Constants.NOJSONCALLBACK);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), FullPictureActivity.class);
                i.putExtra("url", mListPhoto.get(position).getUrl() );
                startActivity(i);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("search",searchText);
        outState.putInt("position",gridView.getFirstVisiblePosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        searchText = savedInstanceState.getString("search");
        gridView.setSelection(savedInstanceState.getInt("position"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isRestart = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchText != ""){
            parameters.put("text",searchText);
            parameters.put("method",Constants.METHOD_SEARCH);
        }
        if (!isRestart) {
            loadImages(parameters);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText = query;
                parameters.put("text",searchText);
                parameters.put("method",Constants.METHOD_SEARCH);
                loadImages(parameters);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuShowInterestingPic){
            parameters.put("method",Constants.METHOD_INTERESTINGNESS);
            parameters.remove("text");
            searchText = "";
            loadImages(parameters);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadImages(Map<String, String> parameters) {

        mManeger = new RestManager();
        Call<Info> listCall = mManeger.getApiService().getFlickrImg(parameters);
        listCall.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(Call<Info> call, Response<Info> response) {

                mListPhoto = response.body().getPhotos().getPhoto();
                gridView.setAdapter(new Adapter(MainActivity.this, mListPhoto));
            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {

            }
        });

    }
}
