package com.example.freegamesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.freegamesapp.R;
import com.example.freegamesapp.activities.SavedGameInformationCard;
import com.example.freegamesapp.classes.GameOO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapterSavedGames extends RecyclerView.Adapter<RecyclerAdapterSavedGames.ViewHolder> {

    private List<GameOO> gamesList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView gameNameTextView;
        private final ImageView gameImageView;

        public ViewHolder(View view) {
            super(view);
            gameNameTextView = (TextView) view.findViewById(R.id.gameNameTextView);
            gameImageView = (ImageView) view.findViewById(R.id.gameImageView);
        }

        public TextView getTextView() {
            return gameNameTextView;
        }
    }

    public RecyclerAdapterSavedGames(List<GameOO> gamesList, Context context) {
        this.gamesList = gamesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_game_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        GameOO game = gamesList.get(position);

        viewHolder.getTextView().setText(game.getTitle());
        Picasso.with(context.getApplicationContext())
                .load(game.getThumbnail())
                .error(R.color.black)
                .into(viewHolder.gameImageView);


        viewHolder.gameImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent savedGameInformation = new Intent(new Intent(context, SavedGameInformationCard.class));

                String gameReleaseDate = game.getReleaseDate();
                String gamePublisher = game.getPublisher();

                String gameImage = game.getThumbnail();
                String gameName = game.getTitle();
                String gameDesc = game.getShortDescription();
                String gameGenre = game.getGenre();
                String gamePlatform = game.getPlatform();
                String gameURL = game.getGameUrl();

                savedGameInformation.putExtra("GAME_NAME", gameName);
                savedGameInformation.putExtra("GAME_IMAGE", gameImage);
                savedGameInformation.putExtra("GAME_DESC", gameDesc);
                savedGameInformation.putExtra("GAME_GENRE", gameGenre);
                savedGameInformation.putExtra("GAME_PLATFORM", gamePlatform);
                savedGameInformation.putExtra("GAME_PUBLISHER", gamePublisher);
                savedGameInformation.putExtra("GAME_RELEASEDATE", gameReleaseDate);
                savedGameInformation.putExtra("GAME_URL", gameURL);

                context.startActivity(savedGameInformation);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return gamesList.size();
    }
}
