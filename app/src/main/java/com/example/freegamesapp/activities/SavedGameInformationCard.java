package com.example.freegamesapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;

import com.example.freegamesapp.MainActivity;
import com.example.freegamesapp.R;
import com.example.freegamesapp.classes.GameOO;
import com.example.freegamesapp.database.DatabaseManager;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Method;

public class SavedGameInformationCard extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private TextView
            gameNameTextView, gameDescTextView, gameGenreTextView, gamePlatformTextView,
            gameDateReleasTextView, gamePublisherTextView;

    private String
            gameName, gameImage, gameDesc, gameGenre, gamePlatform,
            gameDateRelease, gamePublisher, gameURL;

    private ImageView gameImageView;

    private DatabaseManager databaseManager;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_game_information_card);

        // UI Elements.
        gameNameTextView = (TextView) findViewById(R.id.gameNameTextView);
        gameImageView = (ImageView) findViewById(R.id.gameImageView);
        gameDescTextView = (TextView) findViewById(R.id.gameDescriptionTextView);
        gameGenreTextView = (TextView) findViewById(R.id.gameGenreTextView);
        gamePlatformTextView = (TextView) findViewById(R.id.gamePlatformTextView);

        gamePublisherTextView = (TextView) findViewById(R.id.gamePublisherTextView);
        gameDateReleasTextView = (TextView) findViewById(R.id.gameReleaseDateTextView);

        cardView = (CardView) findViewById(R.id.savedGameInformationCard);
        cardView.setBackgroundColor(Color.rgb(77, 95, 117));

        // Firebase
        databaseManager = new DatabaseManager();

        // Loads the data
        fillData();
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [FEED RECYCLER VIEW]
    public void fillData() {
        gameName = this.getIntent().getStringExtra("GAME_NAME");
        gameImage = this.getIntent().getStringExtra("GAME_IMAGE");
        gameDesc = this.getIntent().getStringExtra("GAME_DESC");
        gameGenre = this.getIntent().getStringExtra("GAME_GENRE");
        gamePlatform = this.getIntent().getStringExtra("GAME_PLATFORM");
        gameDateRelease = this.getIntent().getStringExtra("GAME_RELEASEDATE");
        gamePublisher = this.getIntent().getStringExtra("GAME_PUBLISHER");
        gameURL = this.getIntent().getStringExtra("GAME_URL");

        // Setting the information into the Layout elements.
        gameNameTextView.setText(gameName);
        gameDescTextView.setText(gameDesc);
        gameGenreTextView.setText("Genre: \t" + gameGenre);
        gamePlatformTextView.setText("Plataform: \t" + gamePlatform);
        gameDateReleasTextView.setText("Release Date: \t" + gameDateRelease);
        gamePublisherTextView.setText("Publisher: \t" + gamePublisher);

        // Loading the JSON image into the layout image.
        Picasso.with(this.getApplicationContext())
                .load(gameImage)
                .error(R.color.black)
                .into(gameImageView);
    }

    public void deleteSavedGame() {
        databaseManager.deleteSavedGame(new GameOO.GameOOBuilder().setTitle(gameName).build(), getApplicationContext());
        Toast.makeText(SavedGameInformationCard.this, "The game has been removed.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [FEED MENU]
    public void popupMenuSavedGameInformationCard(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        try {
            Method method = popupMenu.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            method.setAccessible(true);
            method.invoke(popupMenu.getMenu(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.game_saved_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_saved_game_back:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.nav_saved_game_delete:
                deleteSavedGame();
                return true;
            case R.id.nav_saved_game_website:
                visitWebsite();
                return true;
            case R.id.nav_saved_game_share:
                shareGame();
                return true;
        }
        return false;
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [VISIT WEBSITE]
    public void visitWebsite() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gameURL));
        startActivity(browserIntent);
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [SHARE GAME]
    public void shareGame() {
        Intent shareGameIntent = new Intent(Intent.ACTION_SEND);
        shareGameIntent.setType("text/plain");
        shareGameIntent.putExtra(Intent.EXTRA_TEXT, "Hi there! I thought you might like this game: " + gameURL);
        startActivity(shareGameIntent);
    }
}