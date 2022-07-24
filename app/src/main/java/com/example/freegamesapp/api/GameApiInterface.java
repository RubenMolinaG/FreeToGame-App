package com.example.freegamesapp.api;

import com.example.freegamesapp.models.Game;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GameApiInterface {

    @GET("games")
    Call<List<Game>> loadGames();

}
