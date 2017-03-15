package com.example.gross.galleryflickr.controller;

import com.example.gross.galleryflickr.model.Info;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET("services/rest/")
    Call<Info> getFlickrImg(@QueryMap Map<String, String> parameters);

}
