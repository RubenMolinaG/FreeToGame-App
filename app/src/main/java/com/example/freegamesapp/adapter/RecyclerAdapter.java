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
import com.example.freegamesapp.activities.GameInformationCard;
import com.example.freegamesapp.models.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Game> gameList;
    private List<Game> originalGameList;
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


    public RecyclerAdapter(List<Game> gameList, Context context) {
        this.gameList = gameList;
        this.context = context;
        this.originalGameList = new ArrayList<>();
        originalGameList.addAll(gameList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_game_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Game game = gameList.get(position);

        viewHolder.getTextView().setText(game.getTitle());
        Picasso.with(context.getApplicationContext())
                .load(game.getThumbnail())
                .error(R.color.black)
                .into(viewHolder.gameImageView);

        viewHolder.gameImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameBodyIntent = new Intent(new Intent(context, GameInformationCard.class));

                String gameReleaseDate = game.getReleaseDate();
                String gamePublisher = game.getPublisher();

                String gameImage = game.getThumbnail();
                String gameName = game.getTitle();
                String gameDesc = game.getShortDescription();
                String gameGenre = game.getGenre();
                String gamePlatform = game.getPlatform();
                String gameURL = game.getGameUrl();

                gameBodyIntent.putExtra("GAME_NAME", gameName);
                gameBodyIntent.putExtra("GAME_IMAGE", gameImage);
                gameBodyIntent.putExtra("GAME_DESC", gameDesc);
                gameBodyIntent.putExtra("GAME_GENRE", gameGenre);
                gameBodyIntent.putExtra("GAME_PLATFORM", gamePlatform);
                gameBodyIntent.putExtra("GAME_PUBLISHER", gamePublisher);
                gameBodyIntent.putExtra("GAME_RELEASEDATE", gameReleaseDate);
                gameBodyIntent.putExtra("GAME_URL", gameURL);

                context.startActivity(gameBodyIntent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return gameList.size();
    }

    /**
     * Filter by title in the Search Bar.
     *
     * @param strSearch
     */
    public void filter(String strSearch) {
        if (strSearch.length() == 0) {
            gameList.clear();
            gameList.addAll(originalGameList);
        } else {
            List<Game> collect =
                    gameList.stream().filter(i -> i.getTitle().toLowerCase().contains(strSearch)).collect(Collectors.toList());
            gameList.clear();
            gameList.addAll(collect);
        }
        notifyDataSetChanged();
    }

}

