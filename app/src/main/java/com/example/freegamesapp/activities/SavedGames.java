package com.example.freegamesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freegamesapp.MainActivity;
import com.example.freegamesapp.R;
import com.example.freegamesapp.adapter.RecyclerAdapterSavedGames;
import com.example.freegamesapp.classes.GameOO;
import com.example.freegamesapp.database.DatabaseManager;

import java.util.List;

public class SavedGames extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapterSavedGames recyclerAdapter;
    List<GameOO> listSavedGames;
    DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games);

        recyclerView = (RecyclerView) findViewById(R.id.listaJuegosRecyclerView);
        db = new DatabaseManager();
        db.fetchSavedGames(new DatabaseManager.SavedGamesCallback() {
            @Override
            public void savedGamesList(List<GameOO> savedGamesList) {
                if(savedGamesList != null && savedGamesList.size() > 0){
                    listSavedGames = savedGamesList;
                    feedRecyclerView();
                }
            }
        });
    }

    public void feedRecyclerView(){
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);  // Muestra el contenido en dos columnas.
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerAdapterSavedGames(listSavedGames, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void goBackMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}