package com.example.freegamesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freegamesapp.activities.SavedGames;
import com.example.freegamesapp.activities.auth.AuthActivity;
import com.example.freegamesapp.adapter.RecyclerAdapter;
import com.example.freegamesapp.api.APIClient;
import com.example.freegamesapp.api.GameApiInterface;
import com.example.freegamesapp.models.Game;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Method;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, PopupMenu.OnMenuItemClickListener {

    // Log's TAG.
    private String TAG = "MAIN_ACTIVITY";

    // All the objects for the HTTP Request & the RecyclerView
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private SearchView searchView;

    // Firebase Auth & User Control
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    // UI Elements
    private TextView usernameWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Layout UI
        recyclerView = (RecyclerView) findViewById(R.id.listaJuegosRecyclerView);
        searchView = (SearchView) findViewById(R.id.svSearch);
        usernameWelcome = (TextView) findViewById(R.id.tvUsernameWelcome);

        // FireBase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // Welcome User
        usernameWelcome.setText(String.format("Hi there %s!", firebaseUser.getEmail().split("@")[0]));

        // ASYNC Search
        initSearchListener();

        // Feeding the RecyclerView
        retrofit = APIClient.getClient();
        feedRecyclerView();

    }

    // ------------------------------------------------------------------------------------------------------------------------------- [FEED THE RECYCLER VIEW]
    public void feedRecyclerView() {
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);    > Shows the elements in one column.
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);  // > Shows the elements in two columns.
        recyclerView.setLayoutManager(layoutManager);

        GameApiInterface apiService = retrofit.create(GameApiInterface.class);
        Call<List<Game>> gamesList = apiService.loadGames();

        gamesList.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                recyclerAdapter = new RecyclerAdapter(response.body(), MainActivity.this);
                recyclerView.setHasFixedSize(true);

                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [SEARCH BAR - ASYNC]
    public void initSearchListener() {
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        recyclerAdapter.filter(newText.toLowerCase());
        return false;
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [FEED MENU]
    public void feedMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        try {
            Method method = popupMenu.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            method.setAccessible(true);
            method.invoke(popupMenu.getMenu(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.menu_saved_games:
                startActivity(new Intent(this, SavedGames.class));
                return true;
            case R.id.menu_logout:
                logout();
                return true;
        }
        return false;
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [FIREBASE ACTIONS]
    public void logout(){
        // Sign Out Firebase
        firebaseAuth.signOut();

        // Sign out Google
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("797058917152-q5ru2uhat0p6o8udhqk37tqtb7k2dabr.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this, gso);

        client.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this, AuthActivity.class));
                        finish();
                    }
                });

        // Intents Options
        Intent intent = new Intent(this, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
