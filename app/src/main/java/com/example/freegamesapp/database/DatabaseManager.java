package com.example.freegamesapp.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.freegamesapp.classes.GameOO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {

    // Global const.
    private final String TAG = "FIREBASE";

    // Information regarding the game.
    private final String TITLE = "title";
    private final String THUMBNAIL = "thumbnail";
    private final String SHORT_DESC = "short_desc";
    private final String RELEASE_DATE = "release_date";
    private final String PUBLISHER = "publisher";
    private final String PLATFORM = "platform";
    private final String GENRE = "genre";
    private final String GAME_URL = "game_url";

    // Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    // ------------------------------------------------------------------------------------------------------------------------------- [CREATE]
    public void checkIfGameIsAlreadySaved(GameOO game, Context context) {
        db.collection("user_games")
                .document(firebaseUser.getUid())
                .collection("games_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean exists = false;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().containsValue(game.getTitle())) {
                                    exists = true;
                                }
                            }
                            if (!exists) {
                                insertUserGamesRelation(game, context);
                            } else {
                                Toast.makeText(context, "The game is already saved.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void insertUserGamesRelation(GameOO gameOO, Context context) {
        Map<String, Object> game = new HashMap<>();

        game.put(TITLE, gameOO.getTitle());
        game.put(THUMBNAIL, gameOO.getThumbnail());
        game.put(SHORT_DESC, gameOO.getShortDescription());
        game.put(RELEASE_DATE, gameOO.getReleaseDate());
        game.put(PUBLISHER, gameOO.getPublisher());
        game.put(PLATFORM, gameOO.getPlatform());
        game.put(GENRE, gameOO.getGenre());
        game.put(GAME_URL, gameOO.getGameUrl());

        db.collection("user_games")
                .document(firebaseUser.getUid())
                .collection("games_list")
                .add(game)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "The game was saved.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "An error occurred while saving the game.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [READ]
    public interface SavedGamesCallback {
        void savedGamesList(List<GameOO> savedGamesList);
    }

    public void fetchSavedGames(SavedGamesCallback savedGamesCallback) {
        List<GameOO> savedGamesList = new ArrayList<>();

        db.collection("user_games")
                .document(firebaseUser.getUid())
                .collection("games_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                GameOO game = new GameOO.GameOOBuilder()
                                        .setTitle(String.valueOf(document.get(TITLE)))
                                        .setThumbnail(String.valueOf(document.get(THUMBNAIL)))
                                        .setShortDescription(String.valueOf(document.get(SHORT_DESC)))
                                        .setReleaseDate(String.valueOf(document.get(RELEASE_DATE)))
                                        .setPublisher(String.valueOf(document.get(PUBLISHER)))
                                        .setPlatform(String.valueOf(document.get(PLATFORM)))
                                        .setGenre(String.valueOf(document.get(GENRE)))
                                        .setGameURL(String.valueOf(document.get(GAME_URL)))
                                        .build();
                                savedGamesList.add(game);
                            }
                            savedGamesCallback.savedGamesList(savedGamesList);

                        } else {
                            Log.w(TAG, "Error getting saved games.", task.getException());
                        }
                    }
                });
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [DELETE]
    public void deleteSavedGame(GameOO game, Context context) {
        db.collection("user_games")
                .document(firebaseUser.getUid())
                .collection("games_list")
                .whereEqualTo("title", game.getTitle()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                    }
                }
            }
        });
    }
}
